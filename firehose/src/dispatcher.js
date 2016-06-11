export default class Dispatcher{

  var views = [];
  var promises = [];

  register(view){
    views.push(view);
  }

}
