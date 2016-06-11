import React from 'react';
import Connector from './Connector';
import EventReceiver from './EventReceiver';

export default class FireHose extends React.Component{

constructor() {
  super();
  this.receiver = new EventReceiver();
  this.state =  {events: []};
  this.onNewEvent = this.onNewEvent.bind(this);
  this.receiver.setCallback(this.onNewEvent);
}

onNewEvent(event){
  this.setState({events:this.state.events.concat(event)});
}

render(){
  return(
    <div>
      <EventList events={this.state.events}/>
      <Connector listener={this.receiver.setUri}/>
    </div>
    );
  }
}

class EventList extends React.Component{
  render(){
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
}
