The `DisplayAffect` class is a child of `ApplicationFrame`, and creates a
`JFreeChart` with a plot to display `Affect`s. The `update` method on the
`DisplayAffect` class receives a new message from the `Broker` and then has the
`Affect` class parse the `String` into an `Affect` which is then displayed in a
line chart.

The `Affect` class simply has fields for each of the elements of an affect and
getters for those fields.

Incoming data is received as `String`s from the `Broker` class and then parsed
by the `Affect` class.

The `Broker`'s `receive` method is called within `update` of `DisplayAffect` and
the received `String` then parsed, which is how data is updated on the graph.

To test first run the `TestDisplayAffect` program, then run `DisplayAffect`. My
system was already using port 5000 so for testing I changed to port to 5100, but
this change is reverted for the PR.
