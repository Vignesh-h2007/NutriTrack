/* api.js — Shared API helper for all pages */
'use strict';

const API_BASE = '';

// Initial Seed Workout Plans
const INITIAL_WORKOUT_PLANS = [
  { planId: 1, title: "Weight Loss Blitz", description: "High-intensity cardio & bodyweight circuit training designed for maximum calorie burn.", targetGoal: "WEIGHT_LOSS", durationWeeks: 4, estimatedCaloriesBurn: 450 },
  { planId: 2, title: "Hypertrophy Muscle Builder", description: "Heavy compound weight training focused on chest, back, legs, and shoulder volume.", targetGoal: "MUSCLE_GAIN", durationWeeks: 6, estimatedCaloriesBurn: 520 },
  { planId: 3, title: "Full Body HIIT Blast", description: "Fast-paced intervals targeting total body endurance and metabolic conditioning.", targetGoal: "IMPROVED_FITNESS", durationWeeks: 3, estimatedCaloriesBurn: 380 },
  { planId: 4, title: "Core & Abs Sculpt", description: "Targeted abdominal, obliques, and lower back stability circuit.", targetGoal: "MAINTENANCE", durationWeeks: 2, estimatedCaloriesBurn: 260 }
];

// Seed Exercises by Plan
const INITIAL_EXERCISES_BY_PLAN = {
  1: [
    { exerciseId: 101, exerciseName: "Jumping Jacks", category: "Cardio", targetMuscle: "Full Body", sets: 3, reps: 25, durationSeconds: 45, caloriesBurnedPerMin: 10, caloriesBurn: 35, instructions: "Keep feet shoulder-width apart. Jump legs apart while bringing hands above head in a fluid rhythmic arc." },
    { exerciseId: 102, exerciseName: "Bodyweight Squats", category: "Lower Body", targetMuscle: "Quads & Glutes", sets: 3, reps: 15, durationSeconds: 45, caloriesBurnedPerMin: 8, caloriesBurn: 30, instructions: "Lower hips back as if sitting in a chair, keeping chest lifted and knees tracking over toes." },
    { exerciseId: 103, exerciseName: "Push-ups", category: "Upper Body", targetMuscle: "Chest & Triceps", sets: 3, reps: 12, durationSeconds: 45, caloriesBurnedPerMin: 7, caloriesBurn: 28, instructions: "Maintain straight body line from crown to heels. Lower chest to floor and press firmly up." },
    { exerciseId: 104, exerciseName: "Mountain Climbers", category: "Core & Cardio", targetMuscle: "Core & Abs", sets: 3, reps: 20, durationSeconds: 40, caloriesBurnedPerMin: 11, caloriesBurn: 40, instructions: "From a strong plank position, drive knees alternately toward chest at a rapid, controlled cadence." }
  ],
  2: [
    { exerciseId: 201, exerciseName: "Barbell Bench Press", category: "Strength", targetMuscle: "Chest", sets: 4, reps: 8, durationSeconds: 50, caloriesBurnedPerMin: 9, caloriesBurn: 45, instructions: "Grip barbell slightly wider than shoulder width. Lower bar smoothly to mid-chest, then drive upwards." },
    { exerciseId: 202, exerciseName: "Barbell Squats", category: "Strength", targetMuscle: "Legs", sets: 4, reps: 8, durationSeconds: 50, caloriesBurnedPerMin: 10, caloriesBurn: 50, instructions: "Rest bar securely on upper back. Squat deep with straight spine and powerful quad drive." },
    { exerciseId: 203, exerciseName: "Dumbbell Bent-Over Rows", category: "Strength", targetMuscle: "Back & Lats", sets: 3, reps: 10, durationSeconds: 45, caloriesBurnedPerMin: 8, caloriesBurn: 32, instructions: "Hinge at hips with flat back. Pull dumbbells up toward hips squeezing shoulder blades together." },
    { exerciseId: 204, exerciseName: "Overhead Shoulder Press", category: "Strength", targetMuscle: "Shoulders", sets: 3, reps: 10, durationSeconds: 45, caloriesBurnedPerMin: 7, caloriesBurn: 30, instructions: "Press dumbbells vertically overhead until arms lock out, engaging core stability throughout." }
  ],
  3: [
    { exerciseId: 301, exerciseName: "Burpees", category: "HIIT", targetMuscle: "Full Body", sets: 3, reps: 12, durationSeconds: 45, caloriesBurnedPerMin: 12, caloriesBurn: 45, instructions: "Drop to squat thrust, kick feet back, perform push-up, jump back in and leap upwards explosively." },
    { exerciseId: 302, exerciseName: "High Knees", category: "Cardio", targetMuscle: "Cardio & Hip Flexors", sets: 3, reps: 30, durationSeconds: 30, caloriesBurnedPerMin: 10, caloriesBurn: 35, instructions: "Run in place aggressively driving knees up to waist height while pumping arms rhythmically." },
    { exerciseId: 303, exerciseName: "Plank Hold", category: "Core", targetMuscle: "Core", sets: 3, reps: 1, durationSeconds: 60, caloriesBurnedPerMin: 5, caloriesBurn: 25, instructions: "Hold isometric forearm plank keeping glutes squeezed and spine strictly neutral." }
  ],
  4: [
    { exerciseId: 401, exerciseName: "Abdominal Crunches", category: "Core", targetMuscle: "Abs", sets: 3, reps: 20, durationSeconds: 40, caloriesBurnedPerMin: 6, caloriesBurn: 20, instructions: "Contract abdominal wall to curl shoulders off floor, keeping chin lifted off chest." },
    { exerciseId: 402, exerciseName: "Russian Twists", category: "Core", targetMuscle: "Obliques", sets: 3, reps: 20, durationSeconds: 45, caloriesBurnedPerMin: 7, caloriesBurn: 25, instructions: "Elevate heels off ground slightly and rotate shoulders from side to side engaging obliques." },
    { exerciseId: 403, exerciseName: "Leg Raises", category: "Core", targetMuscle: "Lower Abs", sets: 3, reps: 15, durationSeconds: 45, caloriesBurnedPerMin: 6, caloriesBurn: 22, instructions: "Lie flat on back and raise legs straight to 90 degrees, lowering slowly without arching lower back." }
  ]
};

async function apiFetch(path, options = {}) {
  const defaults = {
    headers: { 'Content-Type': 'application/json' },
    credentials: 'same-origin'
  };
  const config = { ...defaults, ...options };
  if (config.body && typeof config.body === 'object') {
    config.body = JSON.stringify(config.body);
  }

  try {
    const res = await fetch(API_BASE + path, config);
    if (res.status === 401) {
      localStorage.removeItem('currentUser');
      throw new Error('Not authenticated');
    }
    if (!res.ok && res.status >= 500) throw new Error(`Server error (${res.status})`);
    const json = await res.json();
    if (!json.success && json.success !== undefined) {
      throw new Error(json.message || json.error || 'Request failed');
    }
    return json.data !== undefined ? json.data : json;
  } catch (err) {
    if (err.message === 'Not authenticated') {
      throw err;
    }
    console.warn(`[NutriTrack] Using dynamic offline storage mode for ${path}`);
    return handleFallback(path, config, options);
  }
}

/* ── Smart Fallback & Local Storage Persistence Engine ── */
function handleFallback(path, config, options) {
  const method = (config.method || 'GET').toUpperCase();
  let parsedBody = {};
  if (options.body) {
    parsedBody = typeof options.body === 'string' ? JSON.parse(options.body) : options.body;
  }

  // ────── Auth & User Profile ──────
  if (path.includes('/api/auth/register')) {
    const newUser = {
      userId: Math.floor(Math.random() * 1000) + 1,
      name: parsedBody.name || 'User',
      email: parsedBody.email || 'user@example.com',
      gender: parsedBody.gender || 'Male',
      age: parseInt(parsedBody.age) || 25,
      heightCm: parseFloat(parsedBody.heightCm) || 170,
      weightKg: parseFloat(parsedBody.weightKg) || 70,
      activityLevel: parsedBody.activityLevel || 'MODERATE',
      healthGoal: parsedBody.healthGoal || 'WEIGHT_LOSS',
      hasMedicalCondition: parsedBody.hasMedicalCondition || false,
      medicalConditions: parsedBody.medicalConditions || [],
      otherMedicalNotes: parsedBody.otherMedicalNotes || ''
    };
    localStorage.setItem('currentUser', JSON.stringify(newUser));
    // Initialize initial weight log using the user's entered registration weight!
    const initWeight = [{ logId: Date.now(), weightKg: newUser.weightKg, logDate: today(), notes: 'Starting weight' }];
    localStorage.setItem('nutri_weight_logs', JSON.stringify(initWeight));
    return Promise.resolve({ success: true, message: 'Account created!', user: newUser });
  }

  if (path.includes('/api/auth/login')) {
    const stored = localStorage.getItem('currentUser');
    if (stored) {
      const user = JSON.parse(stored);
      if (parsedBody.email) user.email = parsedBody.email;
      localStorage.setItem('currentUser', JSON.stringify(user));
      return Promise.resolve({ success: true, user });
    }
    // Fallback demo user
    const demoUser = {
      userId: 1,
      name: "Vignesh H",
      email: parsedBody.email || "vigneshharikumar2007@gmail.com",
      gender: "Male",
      age: 22,
      heightCm: 170,
      weightKg: 70,
      activityLevel: "MODERATE",
      healthGoal: "WEIGHT_LOSS"
    };
    localStorage.setItem('currentUser', JSON.stringify(demoUser));
    return Promise.resolve({ success: true, user: demoUser });
  }

  if (path.includes('/api/auth/logout')) {
    localStorage.removeItem('currentUser');
    return Promise.resolve({ success: true, message: 'Logged out successfully' });
  }

  if (path.includes('/api/auth/me')) {
    const stored = localStorage.getItem('currentUser');
    if (!stored) {
      return Promise.reject(new Error('Not authenticated'));
    }
    const u = JSON.parse(stored);
    if (!u.profileImage && localStorage.getItem('nutri_user_avatar')) {
      u.profileImage = localStorage.getItem('nutri_user_avatar');
    }
    if (!u.dailyWaterTargetMl && localStorage.getItem('nutri_water_target')) {
      u.dailyWaterTargetMl = parseInt(localStorage.getItem('nutri_water_target'));
    }
    return Promise.resolve(u);
  }

  if (path.includes('/api/user')) {
    const stored = localStorage.getItem('currentUser');
    if (!stored) return Promise.reject(new Error('Not authenticated'));
    let currentUser = JSON.parse(stored);

    if (method === 'PUT') {
      if (parsedBody.age) currentUser.age = parseInt(parsedBody.age);
      if (parsedBody.heightCm) currentUser.heightCm = parseFloat(parsedBody.heightCm);
      if (parsedBody.weightKg) {
        currentUser.weightKg = parseFloat(parsedBody.weightKg);
        // Also update weight log
        let weightLogs = JSON.parse(localStorage.getItem('nutri_weight_logs')) || [];
        weightLogs.push({ logId: Date.now(), weightKg: currentUser.weightKg, logDate: today(), notes: 'Profile updated' });
        localStorage.setItem('nutri_weight_logs', JSON.stringify(weightLogs));
      }
      if (parsedBody.gender) currentUser.gender = parsedBody.gender;
      if (parsedBody.activityLevel) currentUser.activityLevel = parsedBody.activityLevel;
      if (parsedBody.healthGoal) currentUser.healthGoal = parsedBody.healthGoal;
      if (parsedBody.name) currentUser.name = parsedBody.name;
      if (parsedBody.email) currentUser.email = parsedBody.email;
      if (parsedBody.medicalConditions) currentUser.medicalConditions = parsedBody.medicalConditions;
      if (parsedBody.dailyWaterTargetMl) {
        currentUser.dailyWaterTargetMl = parseInt(parsedBody.dailyWaterTargetMl);
        localStorage.setItem('nutri_water_target', currentUser.dailyWaterTargetMl);
      }
      if (parsedBody.profileImage !== undefined) {
        currentUser.profileImage = parsedBody.profileImage;
        if (parsedBody.profileImage) {
          localStorage.setItem('nutri_user_avatar', parsedBody.profileImage);
        } else {
          localStorage.removeItem('nutri_user_avatar');
        }
      }

      localStorage.setItem('currentUser', JSON.stringify(currentUser));
      return Promise.resolve({ success: true, user: currentUser });
    }

    if (!currentUser.profileImage && localStorage.getItem('nutri_user_avatar')) {
      currentUser.profileImage = localStorage.getItem('nutri_user_avatar');
    }
    if (!currentUser.dailyWaterTargetMl && localStorage.getItem('nutri_water_target')) {
      currentUser.dailyWaterTargetMl = parseInt(localStorage.getItem('nutri_water_target'));
    }
    return Promise.resolve(currentUser);
  }

  // ────── Water Tracking ──────
  if (path.includes('/api/track/water')) {
    let waterState = JSON.parse(localStorage.getItem('nutri_water_state')) || {
      totalMl: 0,
      logs: []
    };

    if (method === 'POST') {
      const amount = parseInt(parsedBody.amountMl) || 250;
      const notes = parsedBody.notes || '';
      const timeStr = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
      const newLog = { logId: Date.now(), amountMl: amount, notes, logTime: timeStr, loggedDate: today() };
      
      waterState.totalMl += amount;
      waterState.logs.unshift(newLog);
      localStorage.setItem('nutri_water_state', JSON.stringify(waterState));
      return Promise.resolve({ success: true, data: newLog, totalMl: waterState.totalMl });
    }

    if (method === 'DELETE') {
      const parts = path.split('/');
      const deleteId = parseInt(parts[parts.length - 1]);
      const target = waterState.logs.find(l => l.logId === deleteId);
      if (target) {
        waterState.totalMl = Math.max(0, waterState.totalMl - target.amountMl);
        waterState.logs = waterState.logs.filter(l => l.logId !== deleteId);
        localStorage.setItem('nutri_water_state', JSON.stringify(waterState));
      }
      return Promise.resolve({ success: true, totalMl: waterState.totalMl });
    }

    return Promise.resolve(waterState);
  }

  // ────── Workout Tracking ──────
  if (path.includes('/api/workout')) {
    let workoutPlans = JSON.parse(localStorage.getItem('nutri_workout_plans')) || INITIAL_WORKOUT_PLANS;
    let workoutSessions = JSON.parse(localStorage.getItem('nutri_workout_sessions')) || [];

    if (path.includes('/recommend')) {
      const stored = localStorage.getItem('currentUser');
      const u = stored ? JSON.parse(stored) : null;
      const goal = (u && u.healthGoal) ? u.healthGoal : "WEIGHT_LOSS";
      let planKey = 1;
      if (goal === "MUSCLE_GAIN") planKey = 2;
      else if (goal === "IMPROVED_FITNESS") planKey = 3;
      else if (goal === "MAINTENANCE") planKey = 4;
      return Promise.resolve(INITIAL_EXERCISES_BY_PLAN[planKey] || INITIAL_EXERCISES_BY_PLAN[1]);
    }

    if (path.includes('/complete-exercise')) {
      const exName = parsedBody.exerciseName || "Workout Routine";
      const cals = parseInt(parsedBody.caloriesBurned) || 35;
      const sess = {
        sessionId: Date.now(),
        planTitle: exName,
        actualCalories: cals,
        sessionDate: today(),
        status: "COMPLETED"
      };
      workoutSessions.unshift(sess);
      localStorage.setItem('nutri_workout_sessions', JSON.stringify(workoutSessions));
      return Promise.resolve({ success: true, message: 'Exercise completion recorded! 🏆', score: parsedBody.dailyScore || 25 });
    }

    if (path.includes('/plans')) {
      if (method === 'POST') {
        const newPlan = {
          planId: Date.now(),
          title: parsedBody.title || "Custom Workout",
          description: parsedBody.description || "Personalized exercise routine",
          targetGoal: parsedBody.targetGoal || "WEIGHT_LOSS",
          durationWeeks: parseInt(parsedBody.durationWeeks) || 4,
          estimatedCaloriesBurn: parseInt(parsedBody.estimatedCaloriesBurn) || 350
        };
        workoutPlans.unshift(newPlan);
        localStorage.setItem('nutri_workout_plans', JSON.stringify(workoutPlans));
        return Promise.resolve(newPlan);
      }
      return Promise.resolve(workoutPlans);
    }

    if (path.includes('/exercises')) {
      const planId = options.planId || 1;
      const list = INITIAL_EXERCISES_BY_PLAN[planId] || INITIAL_EXERCISES_BY_PLAN[1];
      return Promise.resolve(list);
    }

    if (path.includes('/sessions')) {
      if (method === 'POST') {
        const planId = parsedBody.planId;
        const plan = workoutPlans.find(p => p.planId === planId) || workoutPlans[0];
        const newSession = {
          sessionId: Date.now(),
          planId: planId,
          planTitle: plan ? plan.title : "Workout Session",
          sessionDate: today(),
          status: "IN_PROGRESS",
          actualCalories: plan ? plan.estimatedCaloriesBurn : 350
        };
        workoutSessions.unshift(newSession);
        localStorage.setItem('nutri_workout_sessions', JSON.stringify(workoutSessions));
        return Promise.resolve(newSession);
      }

      if (method === 'PUT') {
        const { sessionId, status } = parsedBody;
        const session = workoutSessions.find(s => s.sessionId === sessionId);
        if (session) {
          session.status = status || "COMPLETED";
          localStorage.setItem('nutri_workout_sessions', JSON.stringify(workoutSessions));
        }
        return Promise.resolve({ success: true, session });
      }

      return Promise.resolve(workoutSessions);
    }
  }

  // ────── Food & Meal ──────
  if (path.includes('/api/food')) {
    const foods = [
      { foodId: 1, name: "Grilled Chicken Breast", category: "PROTEIN", calories: 165, protein: 31, carbs: 0, fat: 3.6 },
      { foodId: 2, name: "Oatmeal with Berries & Chia", category: "CARBS", calories: 220, protein: 6, carbs: 42, fat: 3.5 },
      { foodId: 3, name: "Greek Yogurt Bowl", category: "DAIRY", calories: 150, protein: 15, carbs: 12, fat: 2 },
      { foodId: 4, name: "Avocado & Egg Toast", category: "SNACKS", calories: 250, protein: 9, carbs: 22, fat: 14 },
      { foodId: 5, name: "Salmon Fillet & Steamed Greens", category: "PROTEIN", calories: 206, protein: 22, carbs: 2, fat: 12 },
      { foodId: 6, name: "Steamed Broccoli & Lemon", category: "VEGETABLES", calories: 55, protein: 3.7, carbs: 11, fat: 0.6 },
      { foodId: 7, name: "Brown Rice & Lentil Dal", category: "CARBS", calories: 215, protein: 8, carbs: 41, fat: 2.2 },
      { foodId: 8, name: "Mixed Fresh Berries Bowl", category: "FRUITS", calories: 84, protein: 1.1, carbs: 21, fat: 0.5 }
    ];
    if (path.includes('/search')) {
      const qMatch = path.match(/q=([^&]*)/);
      const q = qMatch ? decodeURIComponent(qMatch[1]).toLowerCase() : '';
      return Promise.resolve(foods.filter(f => f.name.toLowerCase().includes(q)));
    }
    return Promise.resolve(foods);
  }

  if (path.includes('/api/meal')) {
    let mealLogs = JSON.parse(localStorage.getItem('nutri_meal_logs')) || [];

    if (path.includes('/complete')) {
      const match = path.match(/\/logs\/(\d+)\/complete/);
      if (match) {
        const logId = parseInt(match[1]);
        const target = mealLogs.find(m => m.logId === logId);
        if (target) {
          target.status = 'COMPLETED';
          localStorage.setItem('nutri_meal_logs', JSON.stringify(mealLogs));
        }
        return Promise.resolve({ success: true, message: 'Meal marked as completed' });
      }
    }

    if (method === 'DELETE' && path.includes('/logs/')) {
      const parts = path.split('/');
      const delId = parseInt(parts[parts.length - 1]);
      mealLogs = mealLogs.filter(m => m.logId !== delId);
      localStorage.setItem('nutri_meal_logs', JSON.stringify(mealLogs));
      return Promise.resolve({ success: true, message: 'Meal deleted' });
    }

    if (method === 'POST') {
      const newMeal = {
        logId: Date.now(),
        foodId: parsedBody.foodId || 1,
        foodName: parsedBody.foodName || "Custom Food",
        mealType: parsedBody.mealType || "LUNCH",
        quantityGrams: parsedBody.quantityGrams || 100,
        calories: parsedBody.calories || 200,
        protein: parsedBody.protein || 15,
        carbs: parsedBody.carbs || 20,
        fat: parsedBody.fat || 5,
        status: "PENDING",
        loggedDate: parsedBody.logDate || today()
      };
      mealLogs.unshift(newMeal);
      localStorage.setItem('nutri_meal_logs', JSON.stringify(mealLogs));
      return Promise.resolve(newMeal);
    }

    return Promise.resolve(mealLogs);
  }

  // ────── Track Weight ──────
  if (path.includes('/api/track/weight')) {
    const stored = localStorage.getItem('currentUser');
    const curU = stored ? JSON.parse(stored) : null;
    const baseWeight = (curU && curU.weightKg) ? parseFloat(curU.weightKg) : 70.0;
    
    let weightLogs = JSON.parse(localStorage.getItem('nutri_weight_logs'));
    if (!weightLogs || weightLogs.length === 0) {
      weightLogs = [
        { logId: 1, weightKg: baseWeight, logDate: today(), recordedAt: today(), notes: "Initial starting weight" }
      ];
      localStorage.setItem('nutri_weight_logs', JSON.stringify(weightLogs));
    }

    if (method === 'POST') {
      const enteredW = parseFloat(parsedBody.weightKg) || baseWeight;
      const newWeight = {
        logId: Date.now(),
        weightKg: enteredW,
        logDate: parsedBody.logDate || today(),
        recordedAt: parsedBody.logDate || today(),
        notes: parsedBody.notes || ''
      };
      weightLogs.push(newWeight);
      localStorage.setItem('nutri_weight_logs', JSON.stringify(weightLogs));
      if (curU) {
        curU.weightKg = enteredW;
        localStorage.setItem('currentUser', JSON.stringify(curU));
      }
      return Promise.resolve(newWeight);
    }

    return Promise.resolve(weightLogs);
  }

  if (path.includes('/api/track/bmi')) {
    const wMatch = path.match(/weightKg=([^&]*)/);
    const hMatch = path.match(/heightCm=([^&]*)/);
    const w = wMatch ? parseFloat(wMatch[1]) : 70;
    const h = hMatch ? parseFloat(hMatch[1]) : 170;
    const heightM = h / 100;
    const bmiVal = heightM > 0 ? (w / (heightM * heightM)) : 22.0;
    let cat = "Normal weight";
    if (bmiVal < 18.5) cat = "Underweight";
    else if (bmiVal >= 25 && bmiVal < 30) cat = "Overweight";
    else if (bmiVal >= 30) cat = "Obese";
    return Promise.resolve({ bmi: Math.round(bmiVal * 10) / 10, category: cat });
  }

  // ────── Reports (NO PRESET VALUES — REAL USER LOGS ONLY) ──────
  if (path.includes('/api/report')) {
    let workoutSessions = JSON.parse(localStorage.getItem('nutri_workout_sessions')) || [];
    let mealLogs = JSON.parse(localStorage.getItem('nutri_meal_logs')) || [];
    let waterState = JSON.parse(localStorage.getItem('nutri_water_state')) || { totalMl: 0, logs: [] };

    let startDateStr = today();
    let endDateStr = today();
    if (path.includes('startDate=')) {
      let matchS = path.match(/startDate=([^&]+)/);
      if (matchS) startDateStr = matchS[1];
      let matchE = path.match(/endDate=([^&]+)/);
      if (matchE) endDateStr = matchE[1];
    }

    let dateSeries = [];
    let curDate = new Date(startDateStr);
    let endDate = new Date(endDateStr);

    if (isNaN(curDate.getTime())) curDate = new Date();
    if (isNaN(endDate.getTime())) endDate = new Date();

    while (curDate <= endDate) {
      let dStr = curDate.toISOString().split('T')[0];
      
      // Real burned calories only
      let burned = 0;
      workoutSessions.forEach(s => {
        if (s.status === 'COMPLETED' && s.sessionDate === dStr) {
          burned += (s.actualCalories || 0);
        }
      });

      // Real consumed calories & macros only from completed meals
      let consumed = 0, p = 0, c = 0, f = 0, completedMeals = 0;
      mealLogs.forEach(m => {
        if (m.loggedDate === dStr && (m.status === 'COMPLETED' || !m.status)) {
          consumed += m.calories || 0;
          p += m.protein || 0;
          c += m.carbs || 0;
          f += m.fat || 0;
          completedMeals++;
        }
      });

      // Real water only
      let dayWater = 0;
      if (waterState.logs && waterState.logs.length > 0) {
        waterState.logs.forEach(w => {
          if (w.loggedDate === dStr) dayWater += (w.amountMl || 0);
        });
      } else if (dStr === today()) {
        dayWater = waterState.totalMl || 0;
      }

      dateSeries.push({
        date: dStr,
        caloriesConsumed: consumed,
        caloriesBurned: burned,
        proteinGrams: Math.round(p * 10) / 10,
        proteinG: Math.round(p * 10) / 10,
        carbsGrams: Math.round(c * 10) / 10,
        carbsG: Math.round(c * 10) / 10,
        fatGrams: Math.round(f * 10) / 10,
        fatG: Math.round(f * 10) / 10,
        waterMl: dayWater,
        mealsCompleted: completedMeals
      });

      curDate.setDate(curDate.getDate() + 1);
    }

    return Promise.resolve({
      dateSeries: dateSeries
    });
  }

  return Promise.resolve({ success: true });
}

// Auth API Wrapper
const Auth = {
  login:    (email, password) => apiFetch('/api/auth/login',    { method: 'POST', body: { email, password } }),
  register: (data)            => apiFetch('/api/auth/register', { method: 'POST', body: data }),
  logout:   async ()          => {
    try { await apiFetch('/api/auth/logout', { method: 'POST' }); } catch {}
    localStorage.removeItem('currentUser');
    return { success: true };
  },
  me:       ()                => apiFetch('/api/auth/me')
};

// User API Wrapper
const User = {
  getProfile:     () =>           apiFetch('/api/user/'),
  updateProfile:  (data) =>       apiFetch('/api/user/', { method: 'PUT', body: data }),
  changePassword: (data) =>       apiFetch('/api/user/password', { method: 'PUT', body: data }),
  updateAvatar:   async (dataUrl) => {
    if (dataUrl) {
      localStorage.setItem('nutri_user_avatar', dataUrl);
    } else {
      localStorage.removeItem('nutri_user_avatar');
    }
    const stored = localStorage.getItem('currentUser');
    if (stored) {
      const u = JSON.parse(stored);
      u.profileImage = dataUrl || null;
      localStorage.setItem('currentUser', JSON.stringify(u));
    }
    // Attempt backend sync
    try {
      await apiFetch('/api/user/', { method: 'PUT', body: { profileImage: dataUrl || '' } });
    } catch(e) {}
    window.dispatchEvent(new CustomEvent('nutri_avatar_updated', { detail: { avatar: dataUrl } }));
    return { success: true, avatar: dataUrl };
  }
};

// Food API Wrapper
const Food = {
  getAll:       () =>              apiFetch('/api/food/'),
  search:       (q, cat) =>        apiFetch(`/api/food/search?q=${encodeURIComponent(q||'')}&category=${encodeURIComponent(cat||'')}`),
  recommend:    () =>              apiFetch('/api/food/recommend'),
  getById:      (id) =>            apiFetch(`/api/food/${id}`),
  create:       (data) =>          apiFetch('/api/food/', { method: 'POST', body: data })
};

// Meal API Wrapper
const Meal = {
  getPlans:      (date) =>         apiFetch(`/api/meal/plans${date ? '?date='+date : ''}`),
  createPlan:    (data) =>         apiFetch('/api/meal/plans',  { method: 'POST', body: data }),
  deletePlan:    (id) =>           apiFetch(`/api/meal/plans/${id}`, { method: 'DELETE' }),
  getLogs:       (date) =>         apiFetch(`/api/meal/logs${date ? '?date='+date : ''}`),
  logMeal:       (data) =>         apiFetch('/api/meal/logs',   { method: 'POST', body: data }),
  deleteMeal:    (id) =>           apiFetch(`/api/meal/logs/${id}`, { method: 'DELETE' }),
  completeMeal:  (id) =>           apiFetch(`/api/meal/logs/${id}/complete`, { method: 'POST' }),
  calorieTarget: (p) =>            apiFetch(`/api/meal/calorie-target?gender=${p.gender}&heightCm=${p.heightCm}&weightKg=${p.weightKg}&age=${p.age}&activityLevel=${p.activityLevel}`)
};

// Workout API Wrapper
const Workout = {
  getPlans:         () =>          apiFetch('/api/workout/plans'),
  recommend:        () =>          apiFetch('/api/workout/recommend'),
  createPlan:       (data) =>      apiFetch('/api/workout/plans', { method: 'POST', body: data }),
  getExercises:     (planId) =>    apiFetch(`/api/workout/exercises`, { planId }),
  getSessions:      (date) =>      apiFetch(`/api/workout/sessions${date ? '?date='+date : ''}`),
  startSession:     (planId) =>    apiFetch('/api/workout/sessions', { method: 'POST', body: { planId } }),
  logExercise:      (data) =>      apiFetch('/api/workout/log', { method: 'POST', body: data }),
  completeSession:  (id, status) => apiFetch(`/api/workout/sessions`, { method: 'PUT', body: { sessionId: id, status } }),
  completeExercise: (data) =>      apiFetch('/api/workout/complete-exercise', { method: 'POST', body: data })
};

// Track API Wrapper
const Track = {
  getWaterToday:   () =>           apiFetch('/api/track/water/today'),
  logWater:        (amountMl, notes) => apiFetch('/api/track/water', { method: 'POST', body: { amountMl, notes: notes||'' } }),
  deleteWater:     (id) =>         apiFetch(`/api/track/water/${id}`, { method: 'DELETE' }),
  getWaterGoal:    async () => {
    const custom = localStorage.getItem('nutri_water_target');
    if (custom) return parseInt(custom);
    try {
      const u = await Auth.me();
      if (u && u.dailyWaterTargetMl) return u.dailyWaterTargetMl;
      if (u && u.weightKg) return Math.round(u.weightKg * 35);
    } catch(e) {}
    return 100000;
  },
  setWaterGoal:    async (targetMl) => {
    localStorage.setItem('nutri_water_target', targetMl);
    try {
      const u = await Auth.me();
      if (u) {
        u.dailyWaterTargetMl = targetMl;
        localStorage.setItem('currentUser', JSON.stringify(u));
        await User.updateProfile({ ...u, dailyWaterTargetMl: targetMl });
      }
    } catch(e) {}
    window.dispatchEvent(new CustomEvent('nutri_water_goal_updated', { detail: { targetMl } }));
    return { success: true, targetMl };
  },
  logWeight:       (weightKg, logDate, notes) => apiFetch('/api/track/weight', { method: 'POST', body: { weightKg, logDate, notes: notes||'' } }),
  getWeightLogs:   () =>           apiFetch('/api/track/weight/history'),
  getBMI:          (w,h) =>        apiFetch(`/api/track/bmi?weightKg=${w}&heightCm=${h}`)
};

// Report API Wrapper
const Report = {
  getDaily: (start, end) => apiFetch(`/api/report/custom?startDate=${start}&endDate=${end}`).then(res => {
    if (Array.isArray(res)) return res;
    if (res && Array.isArray(res.dateSeries)) return res.dateSeries;
    if (res && res.data && Array.isArray(res.data.dateSeries)) return res.data.dateSeries;
    return [];
  })
};

/* ── Avatar Utilities ── */
function renderUserAvatar(elOrId, user) {
  const el = typeof elOrId === 'string' ? document.getElementById(elOrId) : elOrId;
  if (!el) return;
  const isMenuAvatar = el.id === 'sideAvatar' || el.classList.contains('user-avatar');
  const avatar = (user && user.profileImage) || localStorage.getItem('nutri_user_avatar');
  if (avatar) {
    if (isMenuAvatar) {
      el.style.width = '36px';
      el.style.height = '36px';
      el.style.minWidth = '36px';
      el.style.minHeight = '36px';
      el.style.maxWidth = '36px';
      el.style.maxHeight = '36px';
      el.style.borderRadius = '50%';
      el.style.overflow = 'hidden';
      el.style.flexShrink = '0';
      el.style.display = 'flex';
      el.style.alignItems = 'center';
      el.style.justifyContent = 'center';
      el.innerHTML = `<img src="${avatar}" alt="Avatar" style="width:36px;height:36px;min-width:36px;min-height:36px;max-width:36px;max-height:36px;border-radius:50%;object-fit:cover;display:block;">`;
    } else {
      el.innerHTML = `<img src="${avatar}" alt="Avatar" style="width:100%;height:100%;border-radius:50%;object-fit:cover;display:block;">`;
    }
  } else {
    const name = (user && (user.name || user.fullName)) || 'User';
    el.textContent = name.charAt(0).toUpperCase();
  }
}

function setupGlobalUserSidebar() {
  const stored = localStorage.getItem('currentUser');
  if (!stored) return;
  try {
    const user = JSON.parse(stored);
    const sideAvatar = document.getElementById('sideAvatar');
    if (sideAvatar) renderUserAvatar(sideAvatar, user);
    const sideUserName = document.getElementById('sideUserName');
    if (sideUserName) sideUserName.textContent = user.name || user.fullName || 'User';
    const sideGoal = document.getElementById('sideGoal');
    if (sideGoal && user.goal) {
      sideGoal.textContent = user.goal.replace(/_/g, ' ');
    }
  } catch(e) {}
}

/* ── Smart Water Hydration Alarm & Reminders System ── */
const WaterAlarm = {
  DEFAULT_CONFIG: {
    enabled: false,
    intervalMins: 60,
    remark: "Time to hydrate! Drink a fresh glass of water 💧",
    sound: true,
    notify: true,
    nextAlarmTimestamp: null
  },
  getConfig: function() {
    try {
      const stored = localStorage.getItem('nutri_water_alarm');
      if (stored) {
        return { ...this.DEFAULT_CONFIG, ...JSON.parse(stored) };
      }
    } catch(e) {}
    return { ...this.DEFAULT_CONFIG };
  },
  saveConfig: function(cfg) {
    const current = this.getConfig();
    const updated = { ...current, ...cfg };
    if (updated.enabled) {
      if (!updated.nextAlarmTimestamp || updated.nextAlarmTimestamp <= Date.now()) {
        updated.nextAlarmTimestamp = Date.now() + (updated.intervalMins * 60 * 1000);
      }
    } else {
      updated.nextAlarmTimestamp = null;
    }
    localStorage.setItem('nutri_water_alarm', JSON.stringify(updated));
    window.dispatchEvent(new CustomEvent('nutri_water_alarm_changed', { detail: updated }));
    return updated;
  },
  playChime: function() {
    try {
      const AudioCtx = window.AudioContext || window.webkitAudioContext;
      if (!AudioCtx) return;
      const ctx = new AudioCtx();
      const now = ctx.currentTime;
      
      // Chime Tone 1 (Pleasant pure high note)
      const osc1 = ctx.createOscillator();
      const gain1 = ctx.createGain();
      osc1.type = 'sine';
      osc1.frequency.setValueAtTime(587.33, now); // D5
      osc1.frequency.exponentialRampToValueAtTime(880, now + 0.15); // A5
      gain1.gain.setValueAtTime(0.35, now);
      gain1.gain.exponentialRampToValueAtTime(0.001, now + 0.85);
      osc1.connect(gain1);
      gain1.connect(ctx.destination);
      osc1.start(now);
      osc1.stop(now + 0.85);

      // Chime Tone 2 (Harmonic echo)
      const osc2 = ctx.createOscillator();
      const gain2 = ctx.createGain();
      osc2.type = 'sine';
      osc2.frequency.setValueAtTime(880, now + 0.22); // A5
      osc2.frequency.exponentialRampToValueAtTime(1174.66, now + 0.4); // D6
      gain2.gain.setValueAtTime(0.3, now + 0.22);
      gain2.gain.exponentialRampToValueAtTime(0.001, now + 1.2);
      osc2.connect(gain2);
      gain2.connect(ctx.destination);
      osc2.start(now + 0.22);
      osc2.stop(now + 1.2);
    } catch(e) {
      console.warn('[WaterAlarm] Web Audio API error:', e);
    }
  },
  requestNotificationPermission: async function() {
    if (!("Notification" in window)) {
      showToast('Notifications are not supported in this browser', 'info');
      return false;
    }
    if (Notification.permission === "granted") return true;
    const perm = await Notification.requestPermission();
    if (perm === "granted") {
      showToast('Desktop water notifications enabled! 🔔', 'success');
      return true;
    }
    return false;
  },
  triggerAlert: function(remark) {
    const cfg = this.getConfig();
    const finalRemark = remark || cfg.remark || "Time to hydrate! Drink a fresh glass of water 💧";
    
    if (cfg.sound !== false) {
      this.playChime();
    }
    if (cfg.notify !== false && "Notification" in window && Notification.permission === "granted") {
      try {
        new Notification("💧 NutriTrack Water Reminder", {
          body: finalRemark,
          icon: "favicon.ico"
        });
      } catch(e) {}
    }
    this.showModal(finalRemark);
  },
  showModal: function(remark) {
    let modal = document.getElementById('globalWaterAlarmModal');
    if (!modal) {
      modal = document.createElement('div');
      modal.id = 'globalWaterAlarmModal';
      modal.className = 'app-modal-backdrop';
      modal.innerHTML = `
        <div class="app-modal" style="max-width:440px;text-align:center">
          <div class="modal-header" style="justify-content:center;position:relative">
            <span class="modal-title" style="font-size:1.15rem;color:var(--blue)">💧 Hydration Alarm</span>
            <button class="modal-close-btn" style="position:absolute;right:1rem" onclick="WaterAlarm.dismiss()">✕</button>
          </div>
          <div class="modal-body" style="padding:1.75rem 1.25rem">
            <div class="alarm-alert-icon">💧</div>
            <h2 style="font-size:1.35rem;margin-bottom:.3rem">Time to Drink Water!</h2>
            <div class="alarm-remark-quote" id="globalAlarmRemarkText">"${remark}"</div>
            <p style="font-size:.85rem;color:var(--text-muted);margin-bottom:1.5rem">
              Consistent hydration boosts energy, boosts metabolism, and keeps your mind sharp!
            </p>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:.75rem;margin-bottom:.75rem">
              <button class="btn btn-primary" onclick="WaterAlarm.logAndDismiss(250)" style="background:var(--blue);border-color:var(--blue)">
                🥛 Log 250ml & Reset
              </button>
              <button class="btn btn-secondary" onclick="WaterAlarm.logAndDismiss(500)">
                🍾 Log 500ml & Reset
              </button>
            </div>
            <div style="display:flex;gap:.5rem;justify-content:center">
              <button class="btn btn-secondary btn-sm" onclick="WaterAlarm.snooze(10)" style="font-size:.78rem">
                ⏰ Snooze 10m
              </button>
              <button class="btn btn-secondary btn-sm" onclick="WaterAlarm.dismiss()" style="font-size:.78rem;color:var(--text-muted)">
                Dismiss
              </button>
            </div>
          </div>
        </div>`;
      document.body.appendChild(modal);
    } else {
      const txt = document.getElementById('globalAlarmRemarkText');
      if (txt) txt.textContent = `"${remark}"`;
    }
    setTimeout(() => modal.classList.add('active'), 10);
  },
  closeModal: function() {
    const modal = document.getElementById('globalWaterAlarmModal');
    if (modal) modal.classList.remove('active');
  },
  snooze: function(mins = 10) {
    const cfg = this.getConfig();
    cfg.nextAlarmTimestamp = Date.now() + (mins * 60 * 1000);
    localStorage.setItem('nutri_water_alarm', JSON.stringify(cfg));
    this.closeModal();
    showToast(`Water alarm snoozed for ${mins} minutes 💤`, 'info');
    window.dispatchEvent(new CustomEvent('nutri_water_alarm_changed', { detail: cfg }));
  },
  dismiss: function() {
    const cfg = this.getConfig();
    cfg.nextAlarmTimestamp = Date.now() + (cfg.intervalMins * 60 * 1000);
    localStorage.setItem('nutri_water_alarm', JSON.stringify(cfg));
    this.closeModal();
    showToast(`Next hydration reminder set in ${cfg.intervalMins}m 💧`, 'info');
    window.dispatchEvent(new CustomEvent('nutri_water_alarm_changed', { detail: cfg }));
  },
  logAndDismiss: async function(amountMl = 250) {
    try {
      await Track.logWater(amountMl, 'Hydration Alarm');
      showToast(`Logged ${amountMl}ml water! Stay hydrated! 💧`, 'success');
    } catch(e) {
      showToast('Water logged!', 'success');
    }
    this.dismiss();
    window.dispatchEvent(new CustomEvent('nutri_water_logged', { detail: { amountMl } }));
  },
  initWatcher: function() {
    if (window._waterAlarmWatcherInitialized) return;
    window._waterAlarmWatcherInitialized = true;
    setInterval(() => {
      const cfg = this.getConfig();
      if (!cfg.enabled || !cfg.nextAlarmTimestamp) return;
      const now = Date.now();
      if (now >= cfg.nextAlarmTimestamp) {
        this.triggerAlert(cfg.remark);
        cfg.nextAlarmTimestamp = now + (cfg.intervalMins * 60 * 1000);
        localStorage.setItem('nutri_water_alarm', JSON.stringify(cfg));
        window.dispatchEvent(new CustomEvent('nutri_water_alarm_changed', { detail: cfg }));
      }
    }, 1000);
  }
};

/* ── Live Animated Cosmic Particles & Ethereal Orbs Engine ── */
function initLiveAnimatedBackground() {
  if (document.getElementById('live-ambient-canvas')) return;
  
  const canvas = document.createElement('canvas');
  canvas.id = 'live-ambient-canvas';
  canvas.style.position = 'fixed';
  canvas.style.top = '0';
  canvas.style.left = '0';
  canvas.style.width = '100vw';
  canvas.style.height = '100vh';
  canvas.style.pointerEvents = 'none';
  canvas.style.zIndex = '-1';
  canvas.style.opacity = '0.85';
  
  if (document.body) {
    document.body.insertBefore(canvas, document.body.firstChild);
  } else {
    document.addEventListener('DOMContentLoaded', () => {
      if (!document.getElementById('live-ambient-canvas')) {
        document.body.insertBefore(canvas, document.body.firstChild);
      }
    });
  }

  const ctx = canvas.getContext('2d');
  let width = 0;
  let height = 0;
  let animationFrameId = null;

  function resize() {
    width = canvas.width = window.innerWidth;
    height = canvas.height = window.innerHeight;
  }
  resize();
  window.addEventListener('resize', resize);

  const PARTICLE_COUNT = Math.min(46, Math.max(22, Math.floor((window.innerWidth * window.innerHeight) / 28000)));
  const particles = [];
  const colors = [
    { r: 76,  g: 139, b: 74  },  // Emerald green
    { r: 85,  g: 136, b: 221 },  // Ocean blue
    { r: 93,  g: 165, b: 91  },  // Vibrant accent green
    { r: 229, g: 194, b: 0   }   // Warm gold dust
  ];

  for (let i = 0; i < PARTICLE_COUNT; i++) {
    const col = colors[Math.floor(Math.random() * colors.length)];
    particles.push({
      x: Math.random() * (width || window.innerWidth || 1200),
      y: Math.random() * (height || window.innerHeight || 800),
      vx: (Math.random() - 0.5) * 0.45,
      vy: (Math.random() - 0.5) * 0.45 - 0.12,
      radius: Math.random() * 2.2 + 0.8,
      baseAlpha: Math.random() * 0.35 + 0.22,
      alpha: 0.25,
      phase: Math.random() * Math.PI * 2,
      pulseSpeed: Math.random() * 0.025 + 0.015,
      color: col
    });
  }

  let mouseX = -1000;
  let mouseY = -1000;
  window.addEventListener('mousemove', (e) => {
    mouseX = e.clientX;
    mouseY = e.clientY;
  });
  window.addEventListener('mouseleave', () => {
    mouseX = -1000;
    mouseY = -1000;
  });

  let lastTime = performance.now();

  function animate(now) {
    if (document.hidden) {
      animationFrameId = requestAnimationFrame(animate);
      return;
    }

    const dt = Math.min((now - lastTime) / 1000, 0.1);
    lastTime = now;

    ctx.clearRect(0, 0, width, height);

    for (let i = 0; i < particles.length; i++) {
      const p = particles[i];
      p.phase += p.pulseSpeed;
      p.alpha = p.baseAlpha + Math.sin(p.phase) * 0.16;

      p.x += p.vx * 60 * dt;
      p.y += p.vy * 60 * dt;

      // Mouse soft repulsion
      const dx = p.x - mouseX;
      const dy = p.y - mouseY;
      const dist = Math.sqrt(dx * dx + dy * dy);
      if (dist < 120 && dist > 0) {
        const force = (120 - dist) / 120;
        p.x += (dx / dist) * force * 1.5;
        p.y += (dy / dist) * force * 1.5;
      }

      // Wrap around screen boundaries
      if (p.x < -20) p.x = width + 20;
      if (p.x > width + 20) p.x = -20;
      if (p.y < -20) p.y = height + 20;
      if (p.y > height + 20) p.y = -20;

      // Draw particle with gentle glow
      ctx.beginPath();
      ctx.arc(p.x, p.y, p.radius, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(${p.color.r}, ${p.color.g}, ${p.color.b}, ${Math.max(0, p.alpha)})`;
      ctx.shadowBlur = 8;
      ctx.shadowColor = `rgba(${p.color.r}, ${p.color.g}, ${p.color.b}, 0.55)`;
      ctx.fill();
      ctx.shadowBlur = 0;

      // Connect near particles
      for (let j = i + 1; j < particles.length; j++) {
        const p2 = particles[j];
        const pdist = Math.hypot(p.x - p2.x, p.y - p2.y);
        if (pdist < 110) {
          const lineAlpha = (1 - pdist / 110) * 0.13;
          ctx.beginPath();
          ctx.moveTo(p.x, p.y);
          ctx.lineTo(p2.x, p2.y);
          ctx.strokeStyle = `rgba(${p.color.r}, ${p.color.g}, ${p.color.b}, ${lineAlpha})`;
          ctx.lineWidth = 0.8;
          ctx.stroke();
        }
      }
    }

    animationFrameId = requestAnimationFrame(animate);
  }

  animationFrameId = requestAnimationFrame(animate);
}

/* ── Global Application Settings Manager ── */
const AppSettings = {
  DEFAULT_SETTINGS: {
    theme: 'dark',
    accent: '#4C8B4A',
    auroraBg: true,
    particles: true,
    units: 'metric',
    bmrFormula: 'mifflin',
    privacyMode: false,
    workoutReminders: true,
    mealReminders: true
  },
  getSettings() {
    try {
      const s = localStorage.getItem('nutri_settings');
      return s ? { ...this.DEFAULT_SETTINGS, ...JSON.parse(s) } : { ...this.DEFAULT_SETTINGS };
    } catch(e) {
      return { ...this.DEFAULT_SETTINGS };
    }
  },
  saveSettings(newSettings) {
    try {
      const merged = { ...this.getSettings(), ...newSettings };
      localStorage.setItem('nutri_settings', JSON.stringify(merged));
      this.applySettings(merged);
      window.dispatchEvent(new CustomEvent('nutri_settings_changed', { detail: merged }));
      return merged;
    } catch(e) {
      return this.DEFAULT_SETTINGS;
    }
  },
  applySettings(s = this.getSettings()) {
    try {
      // Apply theme mode
      const theme = s.theme || 'dark';
      document.documentElement.setAttribute('data-theme', theme);
      document.body.classList.remove('theme-dark', 'theme-amoled', 'theme-emerald', 'theme-cyber', 'theme-light');
      document.body.classList.add(`theme-${theme}`);

      // Apply custom accent color
      if (s.accent) {
        document.documentElement.style.setProperty('--ac-green', s.accent);
      }

      // Background toggles
      if (s.auroraBg === false) {
        document.body.classList.add('no-aurora');
      } else {
        document.body.classList.remove('no-aurora');
      }

      const canvas = document.getElementById('live-ambient-canvas');
      if (canvas) {
        canvas.style.display = s.particles === false ? 'none' : 'block';
      }

      // Privacy mode
      if (s.privacyMode) {
        document.body.classList.add('privacy-mode');
      } else {
        document.body.classList.remove('privacy-mode');
      }
    } catch(e) {}
  }
};

// Global init on page load
document.addEventListener('DOMContentLoaded', () => {
  AppSettings.applySettings();
  setupGlobalUserSidebar();
  WaterAlarm.initWatcher();
  initLiveAnimatedBackground();
});
window.addEventListener('nutri_avatar_updated', () => {
  setupGlobalUserSidebar();
});
window.addEventListener('nutri_settings_changed', (e) => {
  AppSettings.applySettings(e.detail);
});

/* ── Toast Notification System ── */
function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }
  const icons = { success: '✅', error: '❌', info: 'ℹ️', warning: '⚠️' };
  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `<span>${icons[type]||'ℹ️'}</span><span>${message}</span>`;
  container.appendChild(toast);
  setTimeout(() => { 
    toast.style.opacity = '0';
    toast.style.transition = 'opacity 0.3s';
    setTimeout(() => toast.remove(), 350); 
  }, 3500);
}

/* ── Session Guard ── */
async function requireAuth() {
  try {
    const user = await Auth.me();
    return user;
  } catch {
    window.location.href = 'login.html';
    return null;
  }
}

/* ── Date Helpers ── */
function formatDate(d) { return new Date(d).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' }); }
function today() { return new Date().toISOString().split('T')[0]; }
function todayFormatted() { return new Date().toLocaleDateString('en-IN', { weekday:'long', day:'numeric', month:'long', year:'numeric' }); }

