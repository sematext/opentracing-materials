const http = require('http');
const port = 3000;

const requestHandler = (request, response) => {
  console.log(request.url);
  response.end('Hello Octi');
}

const server = http.createServer(requestHandler);

server.listen(port, (err) => {
  console.log(`server is listening on ${port}`);
});
