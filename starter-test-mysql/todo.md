please check what happens if an error occurred when doing the doProcessData because of the internal error.

please mention in the documentation the database version 5.6
above [https://dev.mysql.com/doc/refman/5.6/en/fractional-seconds.html]

- add aggregator for revert doProcessData as well.
- add the aggregator which was the last exception happens as a param.
- add another feature when doProcessData failed cause to the network error. but the executor says tx do doProcessData later.
- check hikari pool is working well and it will disconnect the connection after server does down. because an error
  occurred too much of connection you have when start the server.
- http://localhost:4200/#/instances?service_name=order-service serach service filed should have a feature to find
  instance from all service. it can be done because we get the instance from the msqyl server.
- add status into database. all thee instances details are saved only status will be changed
- check the websocket maximum message size
- close the redis database connection when the server is down.
- add a validation to check some one has mention sub revert executor as
  ZERO. [because zero is reserved for main revert execution]
- remove the has_process_error from the database. because it is not used. 
