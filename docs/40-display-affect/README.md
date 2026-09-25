The `DisplayAffect` class is a child of `ApplicationFrame`, and creates a
`JFreeChart` with a plot to display `Affect`s. The `update` method of
`DisplayAffect` takes a new `Affect` to append to the plot.

The `Affect` class simply has fields for each of the elements of an affect and
getters for those fields.

Incoming data is received as `String`s from the `Broker` class and then parsed
by the `Affect` class.

The `Broker`'s `receive` method can be called and the output given to
`Affect.fromString` and the created affect given to `update` of `DisplayAffect`,
which is how data is updated on the graph.

To test first run the `TestDisplayAffect` program, then run `DisplayAffect`. My
system was already using port 5000 so for testing I changed to port to 5100, but
this change is reverted for the PR.
