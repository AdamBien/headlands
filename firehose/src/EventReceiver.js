export default class EventReceiver{


  constructor(){
    this.setUri = this.setUri.bind(this);
    this.setCallback = this.setCallback.bind(this);
    this.serialize = this.serialize.bind(this);
    this.eventCounter = 0;
  }

  setUri(uri){
    this.uri = uri;
    this.connect();
  }

  setCallback(callback){
    this.callback = callback;
  }

  serialize(stringified,callback){
    let array = JSON.parse(stringified);
    for(let event of array){
      event.id = this.eventCounter++;
      callback(event);
    }
  }

  connect(){
    let socket = new WebSocket(`ws://${this.uri}/headlands/firehose/*`);
   socket.onmessage =  (event) => {
     this.serialize(event.data,this.callback);
   };
   socket.onopen = (event) => {
     console.log("socket connection established",event);
   };
 }
}
