# Thoughts

## Replacing the Report object (2016-03-23)

I have been thinking about trying to implement using `JavaPairRDD<cKey, Value>`
instead of `JavaRDD<Report>`.

Positive
- I can use the native `filter`, `flatmap`, `collect` etc
- *May* be more efficient to use the methods above compared to mine
- Better scaling

Negative
- Will need more memory (will probably be a lot more rows.) This probably wont matter since we are using tools from BigData
- Don't know how, or if, I can return multiple `JavaPairRDD`s from the simulation `map`. Probably wont work as I want it to.

Other
- Could it work with the `StreamAPI` in Flink?
- `DataSet` / `DataFrame`?
- Maybe even `JavaPairRDD<String, Tuple2<cKey, Value>>`?