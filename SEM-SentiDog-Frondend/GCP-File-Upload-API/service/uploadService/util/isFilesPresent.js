const processFileMiddleware = require("./uploadmiddlware");

async function isFilesPresent(req, res, next) {
  try {
    await processFileMiddleware(req, res);

    if (!req.file) {
      res.status(400).send({ message: "Please upload a file!" });
    } else {
      next();
    }
  } catch (e) {
    res.status(500).send({ message: "a server error occured!" });
  }
}

module.exports = isFilesPresent;
