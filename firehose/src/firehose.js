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
  console.log(event);
  this.setState({events:this.state.events.concat(event)});
}


render(){
  return(
    <div>
      <Connector listener={this.receiver.setUri}/>
      <EventList events={this.state.events}/>
    </div>
    );
  }
}

class EventList extends React.Component{
  render(){
    return (
      <table>
        <thead>
        <tr>
            <th>Cache Name</th><th>Key</th><th>Value</th>
        </tr>
      </thead>
      <tbody>
        {
          this.props.events.map(function(event){
            return <tr key={event.id}><td>{event.cacheName}</td><td>{event.key}</td><td>{event.value}</td></tr>;
          })
        }
        </tbody>
  </table>
  );
  }
}
