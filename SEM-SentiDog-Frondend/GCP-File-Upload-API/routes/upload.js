const express = require("express");
const uploadService = require("../service/uploadService");
const isFilesPresent = require("../service/uploadService/util/isFilesPresent");

const router = express.Router();

/**
 * upload file route
 * method: post
 */
router.post("/", [isFilesPresent, uploadService, (req, res) => {
  const { response } = req;
  res.status(200).send(response);
}]);

module.exports = router;
