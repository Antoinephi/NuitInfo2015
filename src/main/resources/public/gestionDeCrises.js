// Init viewer
var viewer = new Cesium.Viewer('cesiumContainer');

// Get location request
var helps = [];

$.get("http://172.18.13.123:8080/location", function(data) {
  //console.log($.parseJSON(data));
  json_data = $.parseJSON(data);

  $.each(json_data, function(index){
    //console.log(json_data[index].client_id);
    //console.log(json_data[index].latitude);
    //console.log(json_data[index].longitude);

    helps.push(viewer.entities.add({
      name : json_data[index].client_id,
      position : Cesium.Cartesian3.fromDegrees(parseFloat(json_data[index].longitude), parseFloat(json_data[index].latitude)),
      description : 'Help !',
      point : {
        pixelSize : 5,
        color : Cesium.Color.RED,
        outlineColor : Cesium.Color.WHITE,
        outlineWidth : 2
      },
      label : {
        text : json_data[index].client_id + '',
        font : '14pt monospace',
        style: Cesium.LabelStyle.FILL_AND_OUTLINE,
        outlineWidth : 2,
        verticalOrigin : Cesium.VerticalOrigin.BOTTOM,
        pixelOffset : new Cesium.Cartesian2(0, -9),
        show: false
      }
    })); 
  });

});

// Mouse handler
var handler = new Cesium.ScreenSpaceEventHandler(viewer.canvas);

handler.setInputAction(function(movement) {
  var pickedEntity = viewer.scene.pick(movement.endPosition);
  helps.forEach(function(help) {
    if(pickedEntity && pickedEntity.id == help) {
      help.label.show = true;
    } else {
      help.label.show = false;
    }
  });
}, Cesium.ScreenSpaceEventType.MOUSE_MOVE);

// Add autocomplete to search bar
var search = document.getElementById('searchBarInput');

helpsName = [];
helps.forEach(function(help) {
  helpsName.push(help.name);
});

$('#searchBarInput').autocomplete({
  source: helpsName
});

// Search
$('#searchBarInput').keyup(function(event) {
  if(event.keyCode == 13) {
    helps.some(function(help) {
      if(help.name.toLowerCase().replace(/\s+/g, '').indexOf(search.value.toLowerCase().replace(/\s+/g, '')) > -1) {
        viewer.zoomTo(help, new Cesium.HeadingPitchRange(0, -1, 100000));
        search.value = help.name;
        return true;
      } 
    });

  }
});