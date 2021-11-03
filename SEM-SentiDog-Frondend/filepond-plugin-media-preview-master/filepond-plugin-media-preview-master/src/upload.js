// Register the plugin with FilePond

//     function helper(fileInput) {
//     console.log(fileInput.files[0].name);
//     var formdata = new FormData();
//     formdata.append("file", fileInput.files[0], fileInput.files[0].name);
//
//     var requestOptions = {
//     method: 'POST',
//     body: formdata,
//     redirect: 'follow'
// };
//
//     fetch("http://localhost:3000/upload", requestOptions)
//     .then(response => response.text())
//     .then(result => console.log(result))
//     .catch(error => console.log('error', error));
// }

//FilePond.registerPlugin(FilePondPluginMediaPreview, FilePondPluginFileValidateSize);

FilePond.registerPlugin(FilePondPluginMediaPreview);
// Get a reference to the file input element
const inputElement = document.querySelector('input[type="file"]');

// Create the FilePond instance

// var mockServer = {
//     remove: null,
//     revert: null,
//     process: function(fieldName, file, metadata, load, error, progress, abort, transfer, options) {
//
//         var prog = 0;
//         var total = file.size;
//         var speed = 500 * 1024; // KB/s
//         var aborted = false;
//
//         const tick = function() {
//
//             if (aborted) return;
//
//             prog += Math.random() * speed;
//             prog = Math.min(total, prog);
//
//             progress(true, prog, total);
//
//             if (prog === total) {
//                 load(Date.now());
//                 return;
//             }
//
//             setTimeout(tick, Math.random() * 250)
//         }
//
//         tick();
//
//         return {
//             abort: function() {
//                 aborted = true;
//
//                 abort()
//             }
//         }
//     }
// };
// const pond = FilePond.create(inputElement,{
//      maxFileSize: '200MB',
//     server: 'http://localhost.3000/upload'
//  });



const pond = FilePond.create(inputElement);
//pond.addFile('./Somya-test.mp4');
//pond.addFile('./woodpecker.mp3');