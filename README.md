# AI_in_Health-Care

It was one of the Project given by University under the subject called "Design Enginnering".
In this subject we were supposed to develop some kind of web service & smartphone app. We were already working on Machine Learning basics and came with an idea to prepare those models and host them on api and access them through Android client app.

In this project we dealt with,

  1) Heart Disease prediction
  2) Breast Cancer Detection
  
So we prepared,

i) FLASK RestFul API delivering the results of models.
  
    both API:
    1) http://intense-citadel-71493.herokuapp.com/heart/predict/
    2) http://intense-citadel-71493.herokuapp.com/cancer/predict/
    
    (dash separated inputs are to be provided further in above URLS)
    
ii) Android Client Application
    
     In GUI we used Cardbased Views,
     For communication with Web Service, We used Volley Lib
     
iii) Datasets
    
     Both datasets from UCI Repository.
     
iv) Models
    
     Built with Sci-kit Learn, Keras, Pandas, Numpy.
     Model persistance: Joblib.
