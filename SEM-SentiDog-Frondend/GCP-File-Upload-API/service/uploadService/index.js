const express = require("express");

const gcpStorageUpload = require("./gcpStorage");

const router = express.Router();

const uploadFile = async (req, res, next) => {
  try {
    req.response = await gcpStorageUpload(req.file);
    next();
  } catch (e) {
    console.error(e);
    res.status(500).send({ status: "error uploading file" });
  }
};

router.use("/", uploadFile);

module.exports = router;
