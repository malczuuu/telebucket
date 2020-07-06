# Telebucket

Persist telemetry messages in MongoDB using bucket model.

```
{
    "date" : "2020-07-06",
    "name" : "name",
    "since" : 1594048892269558016,
    "size" : 9,
    "until" : 1594048892454284032,
    "records" : [ 
        { "v" : 12.00, "t" : 1594048892269558016, "at" : 1594048892269558016 }, 
        { "v" : 12.33, "t" : 1594048892425519104, "at" : 1594048892425519104 }, 
        { "v" : 12.67, "t" : 1594048892429788160, "at" : 1594048892429788160 }, 
        { "v" : 13.00, "t" : 1594048892432544000, "at" : 1594048892432544000 }, 
        { "v" : 13.33, "t" : 1594048892436172032, "at" : 1594048892436172032 }, 
        { "v" : 13.67, "t" : 1594048892439342848, "at" : 1594048892439342848 }, 
        { "v" : 14.00, "t" : 1594048892442787840, "at" : 1594048892442787840 }, 
        { "v" : 14.33, "t" : 1594048892450636032, "at" : 1594048892450636032 }, 
        { "v" : 14.68, "t" : 1594048892454284032, "at" : 1594048892454284032 }
    ]
}
```

## Configuration

* `telebucket.rabbit-host`
* `telebucket.rabbit-port`
* `telebucket.rabbit-username`
* `telebucket.rabbit-password`
* `telebucket.rabbit-exchange`
* `telebucket.rabbit-exchange-type`
* `telebucket.rabbit-binding-routing-key`
* `telebucket.rabbit-queue`
* `telebucket.mongo-uri`
* `telebucket.mongo-database`
* `telebucket.bucket-size`
