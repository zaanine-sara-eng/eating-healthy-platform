<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Subscriber Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style_subscriber.css">
    <style>
        /* --- MODAL BASE STYLES --- */
        .modal { 
            display: none; 
            position: fixed; 
            z-index: 1000; 
            left: 0; top: 0; 
            width: 100%; height: 100%; 
            background-color: rgba(0,0,0,0.6); 
            backdrop-filter: blur(5px); 
        }
        
        .modal-content { 
            background-color: #fefefe; 
            margin: 5% auto; 
            padding: 25px; 
            border-radius: 15px; 
            width: 90%; 
            max-width: 500px; 
            position: relative; 
            text-align: center;
            max-height: 85vh; 
            overflow-y: auto; 
            box-shadow: 0 5px 30px rgba(0,0,0,0.3);
        }

        .close { color: #aaa; float: right; font-size: 28px; font-weight: bold; cursor: pointer; }
        .close:hover { color: #333; }

        /* --- BMI FORM STYLING --- */
        .bmi-input { width: 80%; padding: 10px; margin: 10px 0; border: 1px solid #ccc; border-radius: 5px; }
        #bmi-result-card { 
            display: none; 
            margin-top: 20px; 
            padding: 15px; 
            border-radius: 10px; 
            background: #f9f9f9; 
            border-left: 5px solid #3498db;
            text-align: left;
        }

        .workout-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 20px; margin-top: 20px; }
        .workout-card { background: white; border: 2px solid #e0e0e0; border-radius: 12px; padding: 15px; cursor: pointer; transition: 0.3s; text-align: center; }
        .workout-card:hover { transform: translateY(-5px); border-color: #4CAF50; box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
        .workout-card.completed { background-color: #d4edda !important; border-color: #28a745 !important; }
        
        #m-video-container {
            width: 100%;
            margin: 15px 0;
            display: flex;
            justify-content: center;
            background: #000;
            border-radius: 10px;
            overflow: hidden;
        }

        #m-video-container video, #m-video-container img {
            max-width: 100%;
            max-height: 300px; 
            height: auto;
            display: block;
        }

        .timer-display { font-size: 2.5rem; font-weight: bold; color: #e74c3c; margin: 10px 0; }
        .notification-item { border-bottom: 1px solid #eee; padding: 10px 0; text-align: left; }
    </style>
</head>
<body>

<div class="dashboard-container">
    <div class="side-menu">
        <h2>Menu</h2>
        <ul>
            <li><a href="updateProfile">Update Profile Info</a></li>
            <li>
                <a href="#" onclick="openModal(); return false;">
                    Notifications 
                    <c:if test="${unreadCount > 0}">
                        <span style="color: red; font-weight: bold;"> (${unreadCount})</span>
                    </c:if>
                </a>
            </li>
            <li><a href="#" onclick="openBmiModal(); return false;" style="color: #3498db; font-weight: bold;">⚖️ BMI Calculator</a></li>
            
            <li><a href="#" onclick="showWorkouts(); return false;" style="color: #4CAF50; font-weight: bold;">🏠 Home Workouts</a></li>
            <li><a href="logout">Logout</a></li>
        </ul>
    </div>

    <div class="main-content">
        <h1>Welcome Back, ${userName}!</h1>
        
        <div id="meal-section">
            <h3>Your Meal Plan for Today (${currentDate})</h3>
            <div class="meal-grid">
                <c:choose>
                    <c:when test="${not empty todayMeals}">
                        <c:forEach var="meal" items="${todayMeals}">
                            <div class="meal-box">
                                <div class="meal-header">
                                    <c:set var="icon" value="🍚"/>
                                    <c:if test="${meal.mealType eq 'Breakfast'}"><c:set var="icon" value="🍳"/></c:if>
                                    <c:if test="${meal.mealType eq 'Lunch'}"><c:set var="icon" value="🥗"/></c:if>
                                    <c:if test="${meal.mealType eq 'Dinner'}"><c:set var="icon" value="🍲"/></c:if>
                                    <c:if test="${meal.mealType eq 'Snack'}"><c:set var="icon" value="🍎"/></c:if>
                                    
                                    <span style="font-size: 1.5rem; margin-right: 10px;">${icon}</span>
                                    <h2>${meal.mealType}</h2>
                                </div>
                                <div class="meal-body">
                                    <p class="meal-description">${meal.mealName}</p>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise><p>Your coach has not yet planned your meals.</p></c:otherwise>
                </c:choose>
            </div>
        </div>

        <div id="workout-section" style="display:none;">
            <h3 style="color: #4CAF50;">Available Home Workouts</h3>
            <div id="api-workout-list" class="workout-grid"></div>
            <button onclick="showMeals()" style="margin-top:20px; background-color: #3498db; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;">Back to Meals</button>
        </div>
    </div>
</div>

<div id="bmiModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeBmiModal()">&times;</span>
        <h2 style="color: #3498db;">⚖️ BMI Calculator</h2>
        <p>This service is provided by our specialized BMI Microservice.</p>
        
        <input type="number" id="weight" class="bmi-input" placeholder="Weight (kg)" step="0.1">
        <input type="number" id="height" class="bmi-input" placeholder="Height (meters, e.g. 1.75)" step="0.01">
        
        <button onclick="calculateBMI()" style="background:#3498db; color:white; padding:10px 20px; border:none; border-radius:8px; cursor:pointer; width: 80%; font-weight: bold;">
            Calculate Now
        </button>

        <div id="bmi-result-card">
            <h4 id="res-category" style="margin: 0; color: #2c3e50;"></h4>
            <p style="margin: 5px 0;"><strong>BMI:</strong> <span id="res-bmi"></span></p>
            <p id="res-tip" style="font-size: 0.9em; color: #555; font-style: italic;"></p>
        </div>
    </div>
</div>

<div id="notificationModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <h2>🔔 Notifications</h2>
        <c:choose>
            <c:when test="${not empty notifications}">
                <c:forEach var="n" items="${notifications}">
                    <div class="notification-item">
                        <p><strong>${n.message}</strong></p>
                        <p style="font-size: 0.8em; color: #888;">
                            <fmt:formatDate value="${n.createdAt}" pattern="MMM dd, yyyy" />
                        </p>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>No new notifications.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div id="activeWorkoutModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeWorkoutModal()">&times;</span>
        <h2 id="modal-title" style="color:#4CAF50; margin-top: 0;"></h2>
        <div id="m-video-container"></div>
        <p id="modal-exercises" style="font-style: italic; color: #666;"></p>
        <div class="timer-display" id="workout-timer">00:30</div>
        <button id="finish-btn" onclick="finishWorkout()" style="background:#28a745; color:white; padding:15px 30px; border:none; border-radius:8px; cursor:pointer; font-weight:bold; font-size:1.1em; width: 100%;">
            I Finished!
        </button>
    </div>
</div>

<script>
    let currentWorkoutDiv = null;
    let timerInterval = null;

    // --- BMI MICROSERVICE LOGIC ---
    function openBmiModal() { document.getElementById('bmiModal').style.display = "block"; }
    function closeBmiModal() { 
        document.getElementById('bmiModal').style.display = "none"; 
        document.getElementById('bmi-result-card').style.display = "none";
    }

    function calculateBMI() {
        const w = document.getElementById('weight').value;
        const h = document.getElementById('height').value;

        if(!w || !h) { alert("Please enter both weight and height!"); return; }

        // Fetching from your second Microservice on Port 8083
        fetch(`http://localhost:8083/api/bmi/calculate?weight=\${w}&height=\${h}`)
            .then(res => res.json())
            .then(data => {
                document.getElementById('bmi-result-card').style.display = "block";
                document.getElementById('res-category').innerText = data.category;
                document.getElementById('res-bmi').innerText = data.bmi;
                document.getElementById('res-tip').innerText = data.tip;
            })
            .catch(err => alert("Error connecting to BMI Microservice. Is it running on port 8083?"));
    }

    // --- WORKOUT LOGIC ---
    function showWorkouts() {
        document.getElementById('meal-section').style.display = 'none';
        document.getElementById('workout-section').style.display = 'block';
        loadWorkoutsFromAPI();
    }

    function showMeals() {
        document.getElementById('workout-section').style.display = 'none';
        document.getElementById('meal-section').style.display = 'block';
    }

    function loadWorkoutsFromAPI() {
        const listDiv = document.getElementById('api-workout-list');
        listDiv.innerHTML = "Loading...";
        fetch('http://localhost:8081/api/workouts')
            .then(res => res.json())
            .then(data => {
                listDiv.innerHTML = "";
                data.forEach(w => {
                    const card = document.createElement('div');
                    card.className = 'workout-card';
                    card.innerHTML = `<h3>\${w.title}</h3><p>\${w.exercises}</p><small>Not started</small>`;
                    card.onclick = () => openWorkoutSession(w, card);
                    listDiv.appendChild(card);
                });
            });
    }

    function openWorkoutSession(workout, element) {
        currentWorkoutDiv = element;
        document.getElementById('modal-title').innerText = workout.title;
        document.getElementById('modal-exercises').innerText = workout.exercises;
        const videoContainer = document.getElementById('m-video-container');
        if (workout.videoUrl && workout.videoUrl.endsWith('.mp4')) {
            videoContainer.innerHTML = `<video autoplay loop muted playsinline><source src="\${workout.videoUrl}" type="video/mp4"></video>`;
        } else {
            videoContainer.innerHTML = `<img src="\${workout.videoUrl || 'https://via.placeholder.com/300?text=No+Video'}">`;
        }
        document.getElementById('activeWorkoutModal').style.display = 'block';
        startTimer(30); 
    }

    function startTimer(seconds) {
        if(timerInterval) clearInterval(timerInterval);
        let timeLeft = seconds;
        const timerNode = document.getElementById('workout-timer');
        timerNode.innerText = "00:30";
        timerInterval = setInterval(() => {
            timeLeft--;
            timerNode.innerText = "00:" + (timeLeft < 10 ? "0" : "") + timeLeft;
            if(timeLeft <= 0) clearInterval(timerInterval);
        }, 1000);
    }

    function finishWorkout() {
        if(currentWorkoutDiv) {
            currentWorkoutDiv.classList.add('completed');
            currentWorkoutDiv.querySelector('small').innerText = "✅ Done!";
        }
        closeWorkoutModal();
        alert("You are crushing it!");
    }

    function closeWorkoutModal() {
        document.getElementById('activeWorkoutModal').style.display = 'none';
        if(timerInterval) clearInterval(timerInterval);
        document.getElementById('m-video-container').innerHTML = '';
    }

    function openModal() { document.getElementById('notificationModal').style.display = "block"; }
    function closeModal() { document.getElementById('notificationModal').style.display = "none"; }
</script>

</body>
</html>