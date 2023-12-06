
function getAllStudents() {
    fetchData('http://localhost:8080/students', 'GET', null, displayStudents);
}

function getStudentById() {
    const studentId = getInputElementValue('studentId');
    if (!studentId) {
        alert('Please enter a valid Student ID.');
        return;
    }

    fetchData(`http://localhost:8080/students/${studentId}`, 'GET', null, displayFoundStudent);
}

function getStudentCoursesById() {
    const studentId = getInputElementValue('studentIdCourses');
    if (!studentId) {
        alert('Please enter a valid Student ID.');
        return;
    }

    fetchData(`http://localhost:8080/students/${studentId}/courses`, 'GET', null, displayFoundStudentCourses);
}

function getAllBooks() {
    fetchData('http://localhost:8080/books', 'GET', null, displayBooks);
}

function getStudentAndAddBook() {
    const studentId = getInputElementValue('addBookStudentId');
    const bookId = getInputElementValue('addBookId');

    if (!studentId || !bookId) {
        alert('Please enter valid Student ID and Book ID.');
        return;
    }

    const url = `http://localhost:8080/students/${studentId}/books/${bookId}`;
    const data = {}; 
    fetchData(url, 'POST', data, displayUpdatedStudent);
}

function fetchData(url, method = 'GET', data = null, callback) {
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            // Outros headers, se necessário
        },
        body: data ? JSON.stringify(data) : null,
    };

    fetch(url, options)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => callback(data))
        .catch(error => console.error('Error:', error));
}
function displayStudents(students) {
    displayList('allStudentsList', students, ['id', 'name']);
}

function displayFoundStudent(student) {
    if (student && student.id) {
        const details = {
            'ID': student.id,
            'Nome': student.name,
            'Cursos': student.courses,
            'Livros': student.books,
            'Disciplinas': student.disciplines
        };
        displayDetails('foundStudentDetails', details);
    } else {
        displayErrorMessage('foundStudentDetails', 'Estudante não encontrado ou dados ausentes.');
    }
}

function displayFoundStudentCourses(courses) {
    displayList('foundStudentCourses', courses, ['id', 'name']);
}

function displayBooks(books) {
    displayList('allBooksList', books, ['id', 'name']);
}

function displayUpdatedStudent(student) {
    displayFoundStudent(student);
}

function displayList(elementId, items, keys) {
    const element = document.getElementById(elementId);
    element.innerHTML = '';

    if (items.length > 0) {
        const list = document.createElement('ul');
        items.forEach(item => {
            const listItem = document.createElement('li');
            listItem.textContent = keys.map(key => `${key}: ${item[key]}`).join(', ');
            list.appendChild(listItem);
        });
        element.appendChild(list);
    } else {
        displayErrorMessage(elementId, 'Nenhum item encontrado.');
    }
}

function displayDetails(elementId, details) {
    const element = document.getElementById(elementId);
    element.innerHTML = '';

    const detailsList = document.createElement('ul');
    for (const key in details) {
        if (details[key]) {
            const listItem = document.createElement('li');
            listItem.textContent = `${key}: ${Array.isArray(details[key]) ? details[key].map(item => item.name).join(', ') : details[key]}`;
            detailsList.appendChild(listItem);
        }
    }

    element.appendChild(detailsList);
}

function displayErrorMessage(elementId, message) {
    const element = document.getElementById(elementId);
    element.textContent = message;
}

function getInputElementValue(elementId) {
    return document.getElementById(elementId).value;
}

function getStudentAndRemoveBook() {
    const studentIdInput = document.getElementById('removeBookStudentId');
    const bookIdInput = document.getElementById('removeBookId');

    const studentId = studentIdInput.value;
    const bookId = bookIdInput.value;

    if (!studentId || !bookId) {
        alert('Please enter valid Student ID and Book ID.');
        return;
    }

    fetch(`http://localhost:8080/students/${studentId}/books/${bookId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                // Remover o livro diretamente da lista na interface do usuário
                const bookListElement = document.getElementById('allBooksList');
                const bookToRemove = bookListElement.querySelector(`li[data-id="${bookId}"]`);
                if (bookToRemove) {
                    bookListElement.removeChild(bookToRemove);
                }

                // Atualizar os detalhes do estudante após a remoção bem-sucedida do livro
                return fetch(`http://localhost:8080/students/${studentId}`);
            } else {
                throw new Error('Failed to remove the book from the student.');
            }
        })
        .then(response => response.json())
        .then(studentData => {
            displayFoundStudent(studentData);
        })
        .catch(error => console.error('Error:', error));
}

function displayRemovedBookDetails(bookDetails) {
    const removedBookDetails = document.getElementById('removedBookDetails');
    removedBookDetails.innerHTML = ''; 

    if (bookDetails.id) {
        const detailsList = document.createElement('ul');

        appendDetailItem(detailsList, `ID: ${bookDetails.id}`);
        appendDetailItem(detailsList, `Nome: ${bookDetails.name}`);

        removedBookDetails.appendChild(detailsList);
    } else {
        removedBookDetails.textContent = 'Erro ao remover o livro do estudante.';
    }
}

function getAllDisciplines() {
    fetch('http://localhost:8080/disciplines')
        .then(response => response.json())
        .then(data => displayDisciplines(data))
        .catch(error => console.error('Error:', error));
}

function displayDisciplines(disciplines) {
    const allDisciplinesList = document.getElementById('allDisciplinesList');
    allDisciplinesList.innerHTML = '';

    if (disciplines.length > 0) {
        disciplines.forEach(discipline => {
            const listItem = document.createElement('li');
            listItem.textContent = `ID: ${discipline.id}, Nome: ${discipline.name}`;
            allDisciplinesList.appendChild(listItem);
        });
    } else {
        allDisciplinesList.textContent = 'Nenhuma disciplina cadastrada.';
    }
}

function getStudentAndAddDiscipline() {
    const studentIdInput = document.getElementById('addDisciplineStudentId');
    const disciplineIdInput = document.getElementById('addDisciplineId');

    const studentId = studentIdInput.value;
    const disciplineId = disciplineIdInput.value;

    if (!studentId || !disciplineId) {
        alert('Please enter valid Student ID and Discipline ID.');
        return;
    }

    fetch(`http://localhost:8080/students/${studentId}/disciplines/${disciplineId}`, {
        method: 'POST',
    })
        .then(response => response.json())
        .then(data => {
            
            return fetch(`http://localhost:8080/students/${studentId}`);
        })
        .then(response => response.json())
        .then(studentData => {
          
            displayFoundStudent(studentData);
        })
        .catch(error => console.error('Error:', error));
}

function displayAddedDisciplineDetails(disciplineDetails) {
    const addedDisciplineDetails = document.getElementById('addedDisciplineDetails');
    addedDisciplineDetails.innerHTML = ''; 
    if (disciplineDetails.id) {
        const detailsList = document.createElement('ul');

        appendDetailItem(detailsList, `ID: ${disciplineDetails.id}`);
        appendDetailItem(detailsList, `Nome: ${disciplineDetails.name}`);

        addedDisciplineDetails.appendChild(detailsList);
    } else {
        addedDisciplineDetails.textContent = 'Erro ao adicionar a disciplina ao estudante.';
    }
}
function getStudentAndRemoveDiscipline() {
    const studentIdInput = document.getElementById('removeDisciplineStudentId');
    const disciplineIdInput = document.getElementById('removeDisciplineId');

    const studentId = studentIdInput.value;
    const disciplineId = disciplineIdInput.value;

    if (!studentId || !disciplineId) {
        alert('Please enter valid Student ID and Discipline ID.');
        return;
    }

    fetch(`http://localhost:8080/students/${studentId}/disciplines/${disciplineId}`, {
        method: 'DELETE',
    })
        .then(response => response.json())
        .then(data => {
        
            return fetch(`http://localhost:8080/students/${studentId}`);
        })
        .then(response => response.json())
        .then(studentData => {
           
            displayFoundStudent(studentData);
        })
        .catch(error => console.error('Error:', error));
}

function displayRemovedDisciplineDetails(disciplineDetails) {
    const removedDisciplineDetails = document.getElementById('removedDisciplineDetails');
    removedDisciplineDetails.innerHTML = ''; 

    if (disciplineDetails.id) {
        const detailsList = document.createElement('ul');

        appendDetailItem(detailsList, `ID: ${disciplineDetails.id}`);
        appendDetailItem(detailsList, `Nome: ${disciplineDetails.name}`);

        removedDisciplineDetails.appendChild(detailsList);
    } else {
        removedDisciplineDetails.textContent = 'Erro ao remover a disciplina do estudante.';
    }
}