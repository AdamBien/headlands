export default class EventReceiver{


  constructor(){
    this.setUri = this.setUri.bind(this);
    this.setCallback = this.setCallback.bind(this);
  }

  setUri(uri){
    this.uri = uri;
    this.connect();
  }

  setCallback(callback){
    this.callback = callback;
  }


  connect(){
    let socket = new WebSocket(`ws://${this.uri}/headlands/firehose/*`);
   socket.onmessage =  (event) => {
     console.log(event);
     this.callback(event);
   };
   socket.onopen = (event) => {
     console.log("socket connection established",event);
   };
 }
}
