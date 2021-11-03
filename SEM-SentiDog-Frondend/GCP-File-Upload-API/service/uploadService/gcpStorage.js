const { Storage } = require("@google-cloud/storage");
const { format } = require("util");

const storageURL = "https://storage.googleapis.com";
const keyFilename = "./service/uploadService/util/auth/gcpKey.json";
const bucketName = process.env.Bucket_Name;

// Instantiate a storage client with credentials
const storage = new Storage({ keyFilename });
const bucket = storage.bucket(bucketName);

const getFolderName = () => {
  // Get today's date
  const date = new Date();

  // Storage Structure: YYYY/MM/DDd
  const folderStructure = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}/`;
  return folderStructure;
};

/**
 * FileUpload function
 * Uploads file into GCP Storage Bucket
 * @param {file}
 */
const uploadFile = async (file) => new Promise((resolve, reject) => {
  try {
    // get file name
    const fileName = file?.originalname || "filename";

    const folder = getFolderName();

    // Create a new blob in the bucket and upload the file data.
    const blob = bucket.file(folder + fileName);
    const blobStream = blob.createWriteStream({
      resumable: false,
    });

    blobStream.on("error", (err) => {
      reject(err.message);
    });

    blobStream.on("finish", async () => {
      const publicUrl = format(`${storageURL}/${bucket.name}/${blob.name}`);

      const uploadInfo = file.upload_response || {};
      uploadInfo.source_url = publicUrl;

      resolve(uploadInfo);
    });

    blobStream.end(file.buffer);
  } catch (error) {
    reject(error);
  }
});

module.exports = uploadFile;
