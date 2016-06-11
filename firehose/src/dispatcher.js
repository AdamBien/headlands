export default class Dispatcher{

  var views = [];

  register(view){
    views.push(view);
  }

  notifyAll(payload){
    for(view of views){
      view.onPayload(payload);
    }
  }
}
