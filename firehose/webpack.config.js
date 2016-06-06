module.exports = {
  entry: './src/index.js',
  output: {
    path: './target/',
    filename: 'archive.js'
  },
  devServer: {
    inline: true,
    port: 8888
  },
  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'babel',
        query: {
          presets: ['es2015', 'react']
        }
      }
    ]
  }
}
