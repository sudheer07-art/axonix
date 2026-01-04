console.log("✅ app.js loaded");

const API_BASE = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", () => {

    // ---------- UPLOAD ----------
    const uploadBtn = document.getElementById("uploadBtn");
    const fileInput = document.getElementById("fileInput");
    const status = document.getElementById("status"); // MUST exist in teacher.html

    if (uploadBtn) {
        uploadBtn.addEventListener("click", () => {
            console.log("🔥 uploadFile() called");

            if (!fileInput || fileInput.files.length === 0) {
                if (status) status.innerText = "❌ Please select a file";
                return;
            }

            const formData = new FormData();
            formData.append("file", fileInput.files[0]);

            fetch(API_BASE + "/files/upload", {
                method: "POST",
                body: formData
            })
            .then(res => {
                if (!res.ok) throw new Error("Upload failed");
                return res.text();
            })
            .then(() => {
                if (status) status.innerText = "✅ File uploaded successfully";
                fileInput.value = "";
            })
            .catch(err => {
                console.error(err);
                if (status) status.innerText = "❌ Upload failed";
            });
        });
    }

    // ---------- STUDENT FILE LIST ----------
    const fileList = document.getElementById("fileList");

    if (fileList) {
        loadFiles();
    }
});

// ================= LOAD FILES =================
//function loadFiles() {
//    fetch("http://localhost:8080/files/list")
//        .then(res => {
//            if (!res.ok) throw new Error("Failed to fetch files");
//            return res.json();
//        })
//        .then(files => {
//            const container = document.getElementById("fileList");
//            container.innerHTML = "";
//
//            if (!Array.isArray(files) || files.length === 0) {
//                container.innerHTML = "<p>No files available.</p>";
//                return;
//            }
//
//            files.forEach(file => {
//                container.innerHTML += `
//                    <div class="file-card">
//                        <span>${file.fileName}</span>
//                        <a class="btn student"
//                           href="http://localhost:8080/files/download/${file.id}">
//                           ⬇ Download
//                        </a>
//                    </div>
//                `;
//            });
//        })
//        .catch(err => {
//            console.error("❌ loadFiles error:", err);
//            document.getElementById("fileList").innerHTML =
//                "<p>Error loading files</p>";
//        });
//}
<script>
    // Load files when page opens
    document.addEventListener("DOMContentLoaded", loadFiles);

    function loadFiles() {
        fetch("/files/list")
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to load files");
                }
                return response.json();
            })
            .then(files => {
                const fileList = document.getElementById("fileList");
                fileList.innerHTML = "";

                if (files.length === 0) {
                    fileList.innerHTML = "<p>No PDFs uploaded yet.</p>";
                    return;
                }

                files.forEach(file => {
                    const div = document.createElement("div");
                    div.className = "file-card";

                    div.innerHTML = `
                        <span>${file.fileName}</span>

                        <a class="btn"
                           href="/files/download/${file.id}">
                           Download
                        </a>

                        <button class="btn danger"
                                onclick="deleteFile(${file.id})">
                            Delete
                        </button>
                    `;

                    fileList.appendChild(div);
                });
            })
            .catch(error => {
                alert(error.message);
            });
    }

    function deleteFile(fileId) {
        if (!confirm("Are you sure you want to delete this PDF?")) {
            return;
        }

        fetch(`/files/delete/${fileId}`, {
            method: "DELETE"
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Delete failed");
            }
            alert("File deleted successfully");
            loadFiles(); // refresh list
        })
        .catch(error => {
            alert(error.message);
        });
    }
</script>

