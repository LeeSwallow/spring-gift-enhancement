let isEdit = false;

const prodEditMode = {
    "prod-name": false,
    "prod-price": false,
    "prod-url": false,
};

const mapIdToKey = {
    "prod-name": "name",
    "prod-price": "price",
    "prod-url": "imageUrl",
};

const prodEditType = {
    "prod-name": "text",
    "prod-price": "number",
    "prod-url": "url",
};

function enableEdit(id) {
    if (prodEditMode[id]) return;
    prodEditMode[id] = true;
    const editArea = document.getElementById(id);
    const textArea =  editArea.querySelector("p");
    const inputArea = editArea.querySelector("input");
    if (inputArea) {
        textArea.hidden = true;
        inputArea.type = prodEditType[id];
        inputArea.hidden = false;
    }
    if (!isEdit) showEditButton();
}

function showEditButton() {
    isEdit = true;
    const editButton = document.getElementById("btn-edit");
    editButton.hidden = false;
}

async function requestEditProduct() {
    const id = document.getElementById("prod-id").textContent;
    const requestBody = {}
    for (const key in prodEditMode) {
        if (prodEditMode[key]) {
            requestBody[mapIdToKey[key]] = document.getElementById(key).querySelector("input").value;
        }
    }
    console.log("수정 요청 본문:", requestBody);
    const res = await fetch(`/api/products/${id}`, {
        "method": "PUT",
        "headers": {
            "Content-Type": "application/json",
            "credentials": "include"
        }
        , "body": JSON.stringify(requestBody)
    });

    if (res.ok) {
        alert("상품이 성공적으로 수정되었습니다!");
        window.location.reload();
    } else {
        const errorData = await res.json();
        console.error("상품 수정 요청 실패:", errorData);
        alert(`상품 수정에 실패했습니다: ${errorData.message}`);
    }
}