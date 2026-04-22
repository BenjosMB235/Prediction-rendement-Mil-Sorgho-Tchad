# Prédiction Rendements Mil/Sorgho - Tchad

## &#x20;Objectif

Système de prédiction des rendements du mil et sorgho au Tchad
utilisant l'imagerie satellite Sentinel-2 et le machine learning.

## &#x20;Pourquoi ce projet ?

* 80% de la production céréalière tchadienne
* 165 000 tonnes perdues annuellement faute de planification
* Solution low-cost pour agriculteurs et coopératives

## &#x20;Zone pilote

3 régions Mandoul, Moyen-Chari et Logone Occidental, Tchad

## &#x20;Stack technique

* NASA MODIS MOD13Q1 (télédétection EVI)
* Google Colab (traitement cloud)
* Python (scikit-learn, pandas, numpy)
* FAO FAOSTAT (données rendements)
* TensorFlow Lite (mobile)
* Android natif Java

## &#x20;Status

* \[x] Setup environnement (Colab + GitHub)
* \[x] Extraction données satellites (MODIS EVI 2010-2024)
* \[x] Feature engineering (7 features, 3 régions, 12 ans)
* \[x] Dataset ML final (36 lignes prêtes)
* \[x] Modèle ML baseline (V2 valide)
* \[x] Features : EVI + Pluviométrie CHIRPS
* \[x] Application Android (inférence TFLite offline)

## &#x20;Partenaires recherchés

* Coopératives agricoles (données terrain)
* Agronomes (validation scientifique)
* Développeurs mobile (collaboration)

## &#x20;Contact

\[mbaibenjsos@gmail.com]

