const express = require("express");

// const path = require("path");
const logger = require("morgan");
const dotenv = require("dotenv");
const bodyParser = require("body-parser");

dotenv.config();
const index = require("./routes/index");
const upload = require("./routes/upload");

const app = express();

app.use(bodyParser.json()); // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({ extended: true }));

// Set morgan to log differently depending on environment
if (app.get("env") !== "production") {
  app.use(logger("common"));
} else {
  app.use(logger("dev"));
}

app.use("/", index);
app.use("/upload", upload);

// catch 404 and forward to error handler
app.use((req, res, next) => {
  const err = new Error("Not Found");
  err.status = 404;
  next(err);
});

// error handler
app.use((err, req, res) => {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error response
  res.status(err.status || 500);
});

module.exports = app;
