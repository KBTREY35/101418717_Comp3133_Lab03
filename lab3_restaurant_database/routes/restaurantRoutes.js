const express = require('express');
const router = express.Router();
const Restaurant = require('../models/Restaurant');

router.get('/', async (req, res) => {
  try {
    const restaurants = await Restaurant.find();
    res.json(restaurants);
  } catch (err) {
    res.status(500).json({ error: 'Error fetching restaurants', details: err.message });
  }
});

router.get('/cuisine/:type', async (req, res) => {
  try {
    const restaurants = await Restaurant.find({ cuisine: req.params.type });
    if (restaurants.length === 0) {
      return res.status(404).json({ message: 'No restaurants found for this cuisine' });
    }
    res.json(restaurants);
  } catch (err) {
    res.status(500).json({ error: 'Error fetching restaurants by cuisine', details: err.message });
  }
});

router.get('/sort', async (req, res) => {
  try {
    const sortBy = req.query.sortBy === 'ASC' ? 1 : -1; 
    const restaurants = await Restaurant.find()
      .select('_id cuisine name city restaurant_id') 
    res.json(restaurants);
  } catch (err) {
    res.status(500).json({ error: 'Error sorting restaurant details', details: err.message });
  }
});

router.get('/Delicatessen', async (req, res) => {
  try {
    const restaurants = await Restaurant.find({
      cuisine: 'Delicatessen',
      city: { $ne: 'Brooklyn' },
    })
      .select('cuisine name city -_id') 
      .sort({ name: 1 }); 
    if (restaurants.length === 0) {
      return res.status(404).json({ message: 'No restaurants found with these criteria' });
    }
    res.json(restaurants);
  } catch (err) {
    res.status(500).json({ error: 'Error fetching Delicatessen restaurants', details: err.message });
  }
});

module.exports = router;
