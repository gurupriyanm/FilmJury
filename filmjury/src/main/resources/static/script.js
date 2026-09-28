const API = "/api";

let entries = [];
let judges = [];
let criteria = [];


// ======================================================
// API HELPER
// ======================================================

async function apiRequest(url, options = {}) {

    const response = await fetch(API + url, {

        ...options,

        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        }

    });


    let data = null;

    try {

        data = await response.json();

    } catch (error) {

        // No JSON response

    }


    if (!response.ok) {

        let message =
            "Something went wrong.";

        if (data && data.message) {

            message = data.message;

        }

        if (data && data.errors) {

            message =
                Object.values(data.errors)
                    .join("\n");

        }

        throw new Error(message);
    }


    return data;
}


// ======================================================
// SECTION NAVIGATION
// ======================================================

function showSection(id) {

    document
        .querySelectorAll(".section")
        .forEach(section => {

            section.classList.remove("active");

        });


    document
        .getElementById(id)
        .classList.add("active");


    if (id === "dashboard") {
        loadDashboard();
    }

    if (id === "entries") {
        loadEntries();
    }

    if (id === "judges") {
        loadJudges();
    }

    if (id === "criteria") {
        loadCriteria();
    }

    if (id === "assignments") {
        loadAssignments();
    }

    if (id === "scoring") {
        loadScoringData();
    }

    if (id === "rankings") {
        loadRankings();
    }
}


// ======================================================
// MESSAGE
// ======================================================

function showMessage(
    elementId,
    message,
    type = "success"
) {

    const element =
        document.getElementById(elementId);


    element.textContent = message;

    element.className =
        "message " + type;


    setTimeout(() => {

        element.textContent = "";

        element.className =
            "message";

    }, 4000);
}


// ======================================================
// DASHBOARD
// ======================================================

async function loadDashboard() {

    try {

        const [
            entryData,
            judgeData,
            criterionData
        ] = await Promise.all([

            apiRequest("/entries"),

            apiRequest("/judges"),

            apiRequest("/criteria")

        ]);


        document.getElementById(
            "entryCount"
        ).textContent =
            entryData.length;


        document.getElementById(
            "judgeCount"
        ).textContent =
            judgeData.length;


        document.getElementById(
            "criterionCount"
        ).textContent =
            criterionData.length;

    } catch (error) {

        console.error(error);

    }
}


// ======================================================
// ENTRIES
// ======================================================

async function loadEntries() {

    try {

        entries =
            await apiRequest("/entries");


        const table =
            document.getElementById(
                "entriesTable"
            );


        table.innerHTML = "";


        entries.forEach(entry => {

            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>${entry.id}</td>

                <td>${entry.title}</td>

                <td>${entry.genre}</td>

                <td>
                    <a
                        href="${entry.videoLink}"
                        target="_blank">
                        Watch
                    </a>
                </td>

                <td>

                    <button
                        class="edit"
                        onclick="
                            editEntry(${entry.id})
                        ">
                        Edit
                    </button>

                    <button
                        class="danger"
                        onclick="
                            deleteEntry(${entry.id})
                        ">
                        Delete
                    </button>

                </td>
            `;


            table.appendChild(row);

        });


        updateEntryDropdowns();

        loadDashboard();

    } catch (error) {

        showMessage(
            "entryMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// SAVE ENTRY
// ======================================================

async function saveEntry() {

    const id =
        document.getElementById(
            "entryId"
        ).value;


    const title =
        document.getElementById(
            "entryTitle"
        ).value.trim();


    const genre =
        document.getElementById(
            "entryGenre"
        ).value.trim();


    const videoLink =
        document.getElementById(
            "entryVideoLink"
        ).value.trim();


    if (!title || !genre || !videoLink) {

        showMessage(
            "entryMessage",
            "Please fill all fields.",
            "error"
        );

        return;
    }


    const entry = {

        title: title,

        genre: genre,

        videoLink: videoLink

    };


    try {

        if (id) {

            await apiRequest(
                `/entries/${id}`,
                {

                    method: "PUT",

                    body:
                        JSON.stringify(entry)

                }
            );


            showMessage(
                "entryMessage",
                "Entry updated successfully."
            );

        } else {

            await apiRequest(
                "/entries",
                {

                    method: "POST",

                    body:
                        JSON.stringify(entry)

                }
            );


            showMessage(
                "entryMessage",
                "Entry added successfully."
            );
        }


        clearEntryForm();

        loadEntries();

    } catch (error) {

        showMessage(
            "entryMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// EDIT ENTRY
// ======================================================

async function editEntry(id) {

    try {

        const entry =
            await apiRequest(
                `/entries/${id}`
            );


        document.getElementById(
            "entryId"
        ).value =
            entry.id;


        document.getElementById(
            "entryTitle"
        ).value =
            entry.title;


        document.getElementById(
            "entryGenre"
        ).value =
            entry.genre;


        document.getElementById(
            "entryVideoLink"
        ).value =
            entry.videoLink;


        document.getElementById(
            "entryFormTitle"
        ).textContent =
            "Update Film";


        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });


    } catch (error) {

        showMessage(
            "entryMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// DELETE ENTRY
// ======================================================

async function deleteEntry(id) {

    if (!confirm(
        "Are you sure you want to delete this entry?"
    )) {

        return;

    }


    try {

        await apiRequest(
            `/entries/${id}`,
            {
                method: "DELETE"
            }
        );


        showMessage(
            "entryMessage",
            "Entry deleted successfully."
        );


        loadEntries();

    } catch (error) {

        showMessage(
            "entryMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// CLEAR ENTRY FORM
// ======================================================

function clearEntryForm() {

    document.getElementById(
        "entryId"
    ).value = "";


    document.getElementById(
        "entryTitle"
    ).value = "";


    document.getElementById(
        "entryGenre"
    ).value = "";


    document.getElementById(
        "entryVideoLink"
    ).value = "";


    document.getElementById(
        "entryFormTitle"
    ).textContent =
        "Add New Film";
}


// ======================================================
// JUDGES
// ======================================================

async function loadJudges() {

    try {

        judges =
            await apiRequest("/judges");


        const table =
            document.getElementById(
                "judgesTable"
            );


        table.innerHTML = "";


        judges.forEach(judge => {

            const assignedCount =
                judge.assignedEntries
                    ? judge.assignedEntries.length
                    : 0;


            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>${judge.id}</td>

                <td>${judge.name}</td>

                <td>${judge.email}</td>

                <td>${assignedCount}</td>

                <td>

                    <button
                        class="edit"
                        onclick="
                            editJudge(${judge.id})
                        ">
                        Edit
                    </button>

                    <button
                        class="danger"
                        onclick="
                            deleteJudge(${judge.id})
                        ">
                        Delete
                    </button>

                </td>

            `;


            table.appendChild(row);

        });


        updateJudgeDropdowns();

        loadDashboard();

    } catch (error) {

        showMessage(
            "judgeMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// SAVE JUDGE
// ======================================================

async function saveJudge() {

    const id =
        document.getElementById(
            "judgeId"
        ).value;


    const name =
        document.getElementById(
            "judgeName"
        ).value.trim();


    const email =
        document.getElementById(
            "judgeEmail"
        ).value.trim();


    if (!name || !email) {

        showMessage(
            "judgeMessage",
            "Please enter name and email.",
            "error"
        );

        return;
    }


    const judge = {

        name: name,

        email: email

    };


    try {

        if (id) {

            await apiRequest(
                `/judges/${id}`,
                {

                    method: "PUT",

                    body:
                        JSON.stringify(judge)

                }
            );


            showMessage(
                "judgeMessage",
                "Judge updated successfully."
            );

        } else {

            await apiRequest(
                "/judges",
                {

                    method: "POST",

                    body:
                        JSON.stringify(judge)

                }
            );


            showMessage(
                "judgeMessage",
                "Judge added successfully."
            );

        }


        clearJudgeForm();

        loadJudges();

    } catch (error) {

        showMessage(
            "judgeMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// EDIT JUDGE
// ======================================================

async function editJudge(id) {

    try {

        const judge =
            await apiRequest(
                `/judges/${id}`
            );


        document.getElementById(
            "judgeId"
        ).value =
            judge.id;


        document.getElementById(
            "judgeName"
        ).value =
            judge.name;


        document.getElementById(
            "judgeEmail"
        ).value =
            judge.email;


        document.getElementById(
            "judgeFormTitle"
        ).textContent =
            "Update Judge";


        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });

    } catch (error) {

        showMessage(
            "judgeMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// DELETE JUDGE
// ======================================================

async function deleteJudge(id) {

    if (!confirm(
        "Are you sure you want to delete this judge?"
    )) {

        return;

    }


    try {

        await apiRequest(
            `/judges/${id}`,
            {
                method: "DELETE"
            }
        );


        showMessage(
            "judgeMessage",
            "Judge deleted successfully."
        );


        loadJudges();

    } catch (error) {

        showMessage(
            "judgeMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// CLEAR JUDGE
// ======================================================

function clearJudgeForm() {

    document.getElementById(
        "judgeId"
    ).value = "";


    document.getElementById(
        "judgeName"
    ).value = "";


    document.getElementById(
        "judgeEmail"
    ).value = "";


    document.getElementById(
        "judgeFormTitle"
    ).textContent =
        "Add New Judge";
}


// ======================================================
// CRITERIA
// ======================================================

async function loadCriteria() {

    try {

        criteria =
            await apiRequest("/criteria");


        const table =
            document.getElementById(
                "criteriaTable"
            );


        table.innerHTML = "";


        criteria.forEach(criterion => {

            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>${criterion.id}</td>

                <td>${criterion.name}</td>

                <td>${criterion.description}</td>

                <td>${criterion.maxScore}</td>

                <td>

                    <button
                        class="edit"
                        onclick="
                            editCriterion(
                                ${criterion.id}
                            )
                        ">
                        Edit
                    </button>

                    <button
                        class="danger"
                        onclick="
                            deleteCriterion(
                                ${criterion.id}
                            )
                        ">
                        Delete
                    </button>

                </td>

            `;


            table.appendChild(row);

        });


        loadDashboard();

    } catch (error) {

        showMessage(
            "criterionMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// SAVE CRITERION
// ======================================================

async function saveCriterion() {

    const id =
        document.getElementById(
            "criterionId"
        ).value;


    const name =
        document.getElementById(
            "criterionName"
        ).value.trim();


    const description =
        document.getElementById(
            "criterionDescription"
        ).value.trim();


    const maxScore =
        Number(
            document.getElementById(
                "criterionMaxScore"
            ).value
        );


    if (!name ||
        !description ||
        !maxScore) {

        showMessage(
            "criterionMessage",
            "Please fill all fields.",
            "error"
        );

        return;
    }


    const criterion = {

        name: name,

        description: description,

        maxScore: maxScore

    };


    try {

        if (id) {

            await apiRequest(
                `/criteria/${id}`,
                {

                    method: "PUT",

                    body:
                        JSON.stringify(criterion)

                }
            );


            showMessage(
                "criterionMessage",
                "Criterion updated successfully."
            );

        } else {

            await apiRequest(
                "/criteria",
                {

                    method: "POST",

                    body:
                        JSON.stringify(criterion)

                }
            );


            showMessage(
                "criterionMessage",
                "Criterion added successfully."
            );

        }


        clearCriterionForm();

        loadCriteria();

    } catch (error) {

        showMessage(
            "criterionMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// EDIT CRITERION
// ======================================================

async function editCriterion(id) {

    try {

        const criterion =
            await apiRequest(
                `/criteria/${id}`
            );


        document.getElementById(
            "criterionId"
        ).value =
            criterion.id;


        document.getElementById(
            "criterionName"
        ).value =
            criterion.name;


        document.getElementById(
            "criterionDescription"
        ).value =
            criterion.description;


        document.getElementById(
            "criterionMaxScore"
        ).value =
            criterion.maxScore;


        document.getElementById(
            "criterionFormTitle"
        ).textContent =
            "Update Criterion";


        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });

    } catch (error) {

        showMessage(
            "criterionMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// DELETE CRITERION
// ======================================================

async function deleteCriterion(id) {

    if (!confirm(
        "Are you sure you want to delete this criterion?"
    )) {

        return;

    }


    try {

        await apiRequest(
            `/criteria/${id}`,
            {
                method: "DELETE"
            }
        );


        showMessage(
            "criterionMessage",
            "Criterion deleted successfully."
        );


        loadCriteria();

    } catch (error) {

        showMessage(
            "criterionMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// CLEAR CRITERION
// ======================================================

function clearCriterionForm() {

    document.getElementById(
        "criterionId"
    ).value = "";


    document.getElementById(
        "criterionName"
    ).value = "";


    document.getElementById(
        "criterionDescription"
    ).value = "";


    document.getElementById(
        "criterionMaxScore"
    ).value = "";


    document.getElementById(
        "criterionFormTitle"
    ).textContent =
        "Add Criterion";
}


// ======================================================
// DROPDOWNS
// ======================================================

function updateJudgeDropdowns() {

    const dropdowns = [

        document.getElementById(
            "assignmentJudge"
        ),

        document.getElementById(
            "scoreJudge"
        )

    ];


    dropdowns.forEach(dropdown => {

        if (!dropdown) return;


        dropdown.innerHTML =
            `<option value="">
                Select Judge
            </option>`;


        judges.forEach(judge => {

            dropdown.innerHTML += `

                <option value="${judge.id}">
                    ${judge.name}
                </option>

            `;

        });

    });
}


function updateEntryDropdowns() {

    const dropdowns = [

        document.getElementById(
            "assignmentEntry"
        ),

        document.getElementById(
            "scoreEntry"
        )

    ];


    dropdowns.forEach(dropdown => {

        if (!dropdown) return;


        dropdown.innerHTML =
            `<option value="">
                Select Film
            </option>`;


        entries.forEach(entry => {

            dropdown.innerHTML += `

                <option value="${entry.id}">
                    ${entry.title}
                </option>

            `;

        });

    });
}


// ======================================================
// ASSIGNMENTS
// ======================================================

async function loadAssignments() {

    try {

        judges =
            await apiRequest("/judges");

        entries =
            await apiRequest("/entries");


        updateJudgeDropdowns();

        updateEntryDropdowns();


        const table =
            document.getElementById(
                "assignmentsTable"
            );


        table.innerHTML = "";


        judges.forEach(judge => {

            if (!judge.assignedEntries) {
                return;
            }


            judge.assignedEntries
                .forEach(entry => {

                    const row =
                        document.createElement("tr");


                    row.innerHTML = `

                        <td>
                            ${judge.name}
                        </td>

                        <td>
                            ${entry.title}
                        </td>

                        <td>

                            <button
                                class="danger"
                                onclick="
                                    removeAssignment(
                                        ${judge.id},
                                        ${entry.id}
                                    )
                                ">
                                Remove
                            </button>

                        </td>

                    `;


                    table.appendChild(row);

                });

        });

    } catch (error) {

        showMessage(
            "assignmentMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// ASSIGN ENTRY
// ======================================================

async function assignEntry() {

    const judgeId =
        document.getElementById(
            "assignmentJudge"
        ).value;


    const entryId =
        document.getElementById(
            "assignmentEntry"
        ).value;


    if (!judgeId || !entryId) {

        showMessage(
            "assignmentMessage",
            "Select both judge and film.",
            "error"
        );

        return;
    }


    try {

        await apiRequest(
            `/judges/${judgeId}/entries/${entryId}`,
            {
                method: "PUT"
            }
        );


        showMessage(
            "assignmentMessage",
            "Film assigned successfully."
        );


        loadAssignments();

    } catch (error) {

        showMessage(
            "assignmentMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// REMOVE ASSIGNMENT
// ======================================================

async function removeAssignment(
    judgeId,
    entryId
) {

    if (!confirm(
        "Remove this assignment?"
    )) {

        return;

    }


    try {

        await apiRequest(
            `/judges/${judgeId}/entries/${entryId}`,
            {
                method: "DELETE"
            }
        );


        showMessage(
            "assignmentMessage",
            "Assignment removed."
        );


        loadAssignments();

    } catch (error) {

        showMessage(
            "assignmentMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// SCORING
// ======================================================

async function loadScoringData() {

    try {

        judges =
            await apiRequest("/judges");

        entries =
            await apiRequest("/entries");

        criteria =
            await apiRequest("/criteria");


        updateJudgeDropdowns();

        updateEntryDropdowns();

        renderScoreFields();

    } catch (error) {

        showMessage(
            "scoreMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// SCORE FIELDS
// ======================================================

function renderScoreFields() {

    const container =
        document.getElementById(
            "scoreFields"
        );


    container.innerHTML = "";


    criteria.forEach(criterion => {

        const row =
            document.createElement("div");


        row.className =
            "score-row";


        row.innerHTML = `

            <div>

                <strong>
                    ${criterion.name}
                </strong>

                <span>
                    / ${criterion.maxScore}
                </span>

            </div>


            <input
                type="number"
                min="0"
                max="${criterion.maxScore}"
                step="0.1"
                data-criterion-id="${criterion.id}"
                placeholder="Score"
            >

        `;


        container.appendChild(row);

    });
}


// ======================================================
// SUBMIT SCORE
// ======================================================

async function submitScore() {

    const judgeId =
        document.getElementById(
            "scoreJudge"
        ).value;


    const entryId =
        document.getElementById(
            "scoreEntry"
        ).value;


    if (!judgeId || !entryId) {

        showMessage(
            "scoreMessage",
            "Select both judge and film.",
            "error"
        );

        return;
    }


    const inputs =
        document.querySelectorAll(
            "[data-criterion-id]"
        );


    const criterionScores = [];


    for (const input of inputs) {

        if (input.value === "") {

            showMessage(
                "scoreMessage",
                "Enter every criterion score.",
                "error"
            );

            return;
        }


        criterionScores.push({

            criterionId:
                Number(
                    input.dataset.criterionId
                ),

            score:
                Number(input.value)

        });

    }


    const scoreCard = {

        judgeId:
            Number(judgeId),

        entryId:
            Number(entryId),

        criterionScores:
            criterionScores

    };


    try {

        await apiRequest(
            "/scorecards",
            {

                method: "POST",

                body:
                    JSON.stringify(scoreCard)

            }
        );


        showMessage(
            "scoreMessage",
            "Scorecard submitted successfully."
        );


        document.getElementById(
            "scoreJudge"
        ).value = "";


        document.getElementById(
            "scoreEntry"
        ).value = "";


        inputs.forEach(input => {

            input.value = "";

        });

    } catch (error) {

        showMessage(
            "scoreMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// RANKINGS
// ======================================================

async function loadRankings() {

    try {

        const rankings =
            await apiRequest("/rankings");


        const table =
            document.getElementById(
                "rankingsTable"
            );


        table.innerHTML = "";


        if (!rankings.length) {

            table.innerHTML = `

                <tr>

                    <td colspan="5">
                        No scored entries yet.
                    </td>

                </tr>

            `;

            return;
        }


        rankings.forEach((ranking, index) => {

            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>
                    ${index + 1}
                </td>

                <td>
                    ${ranking.title}
                </td>

                <td>
                    ${ranking.genre}
                </td>

                <td>
                    ${Number(
                        ranking.averageScore
                    ).toFixed(2)}
                </td>

                <td>
                    ${ranking.numberOfJudges ?? "-"}
                </td>

            `;


            table.appendChild(row);

        });

    } catch (error) {

        showMessage(
            "rankingMessage",
            error.message,
            "error"
        );

    }
}


// ======================================================
// INITIAL LOAD
// ======================================================

loadDashboard();
loadEntries();
loadJudges();
loadCriteria();