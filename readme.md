# Figure.kim Management System module
> Management System of [Figure.kim](http://figure.kim)
- Frontend : Svelte
- Backend : Spring Webflux

## Quick Start
```shell
#mongodb docker compose init
$ docker-compose up --build -d mongodb 

#install app (build frontend and backend)
$ mvn install

#backend execute
$ java -jar ./target/management-*.jar 

#frontend execute 
$ cd ./management-front
$ npm run management-dev

```
q!