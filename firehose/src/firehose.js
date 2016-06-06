import React from 'react';

var FireHose = React.createClass({
connect: function(){
  var socket = new WebSocket("ws://localhost:8080/headlands/firehose/*");
  socket.onopen = function (event) {
  console.log("connection opened", event);
};
socket.onmessage =  (event) => {
  this.setState({events: this.state.events.concat(event)});
  console.log(this.state.events);
};

},
getInitialState: function() {
  return {
    events: []
  };
},
render: function(){
  return(
    <div>
    <EventList events={this.state.events}/>
    <input type="button" value="connect" onClick={this.connect}/>
    </div>
    );
  }
  });

var EventList = React.createClass({
  render: function() {
    return (
      <div>
      <ul>
        {
          this.props.events.map(function(event){
            return <li>{event.data}</li>;
          })
        }
    </ul>
  </div>
);
}
});

export default FireHose
