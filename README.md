# smal.ly

##### Simple URL shortener in Scala using [Finatra](https://twitter.github.io/finatra/).

[![continuous-integration](https://github.com/cacoco/smally-finatra/actions/workflows/ci.yml/badge.svg)](https://github.com/cacoco/smally-finatra/actions/workflows/ci.yml)

### Building

To build:

```
$ sbt compile stage
```

### Running

To run locally using foreman:

```
$ ➔ PORT=8080 REDIS_URL=redis://127.0.0.1:12345 foreman start web
11:29:08 web.1  | started with pid 73394
11:29:09 web.1  | 2017-11-01 11:29:09,931 INF                           Slf4jBridgeUtility$       org.slf4j.bridge.SLF4JBridgeHandler installed.
11:29:10 web.1  | 2017-11-01 11:29:10,610 INF                           HttpMuxer$                HttpMuxer[/admin/metrics.json] = com.twitter.finagle.stats.MetricsExporter(<function1>)
11:29:10 web.1  | 2017-11-01 11:29:10,611 INF                           HttpMuxer$                HttpMuxer[/admin/per_host_metrics.json] = com.twitter.finagle.stats.HostMetricsExporter(<function1>)
11:29:10 web.1  | 2017-11-01 11:29:10,638 INF                           SmallyServerMain$         Process started
11:29:10 web.1  | 2017-11-01 11:29:10,950 INF                           SmallyServerMain$         Serving admin http on 0.0.0.0/0.0.0.0:0
11:29:11 web.1  | 2017-11-01 11:29:11,405 INF                           finagle                   Finagle version 17.10.0 (rev=0c1fa04413999f62d26808eb93e8195535ea73dc) built at 20171026-163548
11:29:12 web.1  | 2017-11-01 11:29:12,385 INF                           SmallyServerMain$         Resolving Finagle clients before warmup
11:29:12 web.1  | 2017-11-01 11:29:12,398 INF                           SmallyServerMain$         Done resolving clients: [].
11:29:12 web.1  | 2017-11-01 11:29:12,400 INF                           FinagleBuildRevision$     Resolved Finagle build revision: (rev=0c1fa04413999f62d26808eb93e8195535ea73dc)
11:29:12 web.1  | 2017-11-01 11:29:12,577 INF                           SmallyServerMain$         Warming up.
11:29:12 web.1  | 2017-11-01 11:29:12,705 INF                           HttpRouter                Adding routes
11:29:12 web.1  | POST    /url
11:29:12 web.1  | GET     /:id
11:29:12 web.1  | 2017-11-01 11:29:12,778 INF                           SmallyServerMain$         http server started on port: 8080
11:29:12 web.1  | 2017-11-01 11:29:12,779 INF                           SmallyServerMain$         Enabling health endpoint on port 50207
11:29:12 web.1  | 2017-11-01 11:29:12,782 INF                           SmallyServerMain$         io.angstrom.smally.SmallyServerMain started.
11:29:12 web.1  | 2017-11-01 11:29:12,783 INF                           SmallyServerMain$         Startup complete, server ready.
```


To run in [Heroku](https://www.heroku.com): Make sure you have the [Heroku Toolbelt](https://toolbelt.heroku.com/) [installed](https://devcenter.heroku.com/articles/getting-started-with-scala#set-up).

Create a new app in Heroku:

```
$ heroku create
Creating nameless-lake-8055 in organization heroku... done, stack is cedar-14
http://nameless-lake-8055.herokuapp.com/ | https://git.heroku.com/nameless-lake-8055.git
Git remote heroku added
```

Then create a Redis add-on:

```
$ heroku addons:create heroku-redis:hobby-dev
```

Then deploy the example application to Heroku:

```
$ git push heroku master
Counting objects: 480, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (376/376), done.
Writing objects: 100% (480/480), 27.68 MiB | 16.24 MiB/s, done.
Total 480 (delta 101), reused 0 (delta 0)
remote: Compressing source files... done.
remote: Building source:
...
```

Tail the Heroku logs:

```
$ heroku logs -t
```

### Using

To create a new shortened url: post a JSON body to the `/url` endpoint in the form of `{"url":"TO_SHORTEN_URL"}`

```
$ curl -i -H "Content-Type: application/json" -X POST -d '{"url":"http://www.nytimes.com/2012/05/06/travel/36-hours-in-barcelona-spain.html"}' http://127.0.0.1:8080/url
HTTP/1.1 201 Created
Content-Type: application/json; charset=utf-8
Content-Length: 44

{"smally_url":"http://127.0.0.1:8080/9h5k4"}
```

Then in a browser paste the shortened URL to be redirected to the original URL or use [curl](http://curl.haxx.se/docs/manual.html):

```
$ curl -i http://127.0.0.1:8080/9h5k4
HTTP/1.1 301 Moved Permanently
Location: http://www.nytimes.com/2012/05/06/travel/36-hours-in-barcelona-spain.html
Content-Length: 0
```
