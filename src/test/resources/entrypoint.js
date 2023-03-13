var db = connect("mongodb://root:mongo-password@localhost:27017/admin");

db = db.getSiblingDB('figure'); // we can not use "use" statement here to switch db

db.createUser(
    {
        user: "figure",
        pwd: "mongo-password",
        roles: [ { role: "readWrite", db: "figure"} ],
        passwordDigestor: "server",
    }
)