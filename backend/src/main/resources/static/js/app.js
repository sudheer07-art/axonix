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
function loadFiles() {
    fetch("http://localhost:8080/files/list")
        .then(res => {
            if (!res.ok) throw new Error("Failed to fetch files");
            return res.json();
        })
        .then(files => {
            const container = document.getElementById("fileList");
            container.innerHTML = "";

            if (!Array.isArray(files) || files.length === 0) {
                container.innerHTML = "<p>No files available.</p>";
                return;
            }

            files.forEach(file => {
                container.innerHTML += `
                    <div class="file-card">
                        <span>${file.fileName}</span>
                        <a class="btn student"
                           href="http://localhost:8080/files/download/${file.id}">
                           ⬇ Download
                        </a>
                    </div>
                `;
            });
        })
        .catch(err => {
            console.error("❌ loadFiles error:", err);
            document.getElementById("fileList").innerHTML =
                "<p>Error loading files</p>";
        });
}
files.forEach(file => {
    container.innerHTML += `
        <div class="file-card">
            <span>${file.fileName}</span>

            <a href="/files/download/${file.id}" class="btn">
                Download
            </a>

            <button onclick="deleteFile(${file.id})"
                    class="btn danger">
                Delete
            </button>
        </div>
    `;
});
document.getElementById("uploadForm").onsubmit = async e => {
  e.preventDefault();
  const form = new FormData(e.target);

  const res = await fetch("/files/upload", {
    method: "POST",
    body: form
  });

  alert(await res.text());
};