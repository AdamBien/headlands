import React from 'react';

export default class Connector extends React.Component{

  constructor(props){
    super(props);
    this.state = {
      uri: "localhost:8080"
    }
    this.onChange = this.onChange.bind(this);
    this.click = this.click.bind(this);
  }


  click(){
    console.log(`thank you for clicking ${this.state.uri}`);
    this.props.listener(this.state.uri);
  }

  onChange(event){
    let uri = {uri: event.target.value};
    this.setState(uri);
    console.log(uri);
  }

  render(){
    return(
        <div>
          <input type="text" defaultValue={this.state.uri} onChange={this.onChange}/>
          <button onClick={this.click} className="success button">connect</button>
        </div>
      );
  }
}
