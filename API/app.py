from builtins import Exception
from joblib import load

import copy

#For restful api in flask
from flask import Flask,jsonify
from flask_restful import Api, Resource

#For ANN Back-end
import tensorflow as tf

#For matrix
import numpy as np

#To initialise Flask and Graph
app = Flask(__name__)
api = Api(app)
graph = tf.get_default_graph()

@app.route('/')
def hello_world():
    return jsonify(message = 'Invalid URL.')

@app.route('/heart/')
@app.route('/cancer/')
def heart():
    return jsonify(message = 'Invalid URL.')

@app.route('/heart/predict/')
@app.route('/cancer/predict/')
def heart_predict():
    return jsonify(message = 'Invalid URL. Please provide params.')


#loading of data for hear disease
ann = load('static/annFinal.joblib')
knn = load('static/knnFinal.joblib')
dtree = load('static/dtFinal.joblib')
lr = load('static/lrFinal.joblib')
nb = load('static/nbFinal.joblib')
svm = load('static/svmFinal.joblib')

#loading data for breast cancer
bc_knn = load('static/Bc_knn.joblib')
bc_lr = load('static/Bc_lr.joblib')
bc_nb = load('static/Bc_nb.joblib')
bc_svm = load('static/Bc_svm.joblib')

#api for heart prediction
class HeartAPI(Resource):
    
    def get(self,params):
        
        global ann,knn,dtree,lr,nb,svm,graph

        try:
            #Taking params into a list
            params = params.split('-')
            params = [float(f) for f in params]

            if len(params)!=13:
                raise Exception('You need to enter 13 params')

            #If everything is right, then predict
            else:
                params = np.array(params)
                
                #Feature scaling                
                std_list = np.array([9.06710164,0.46524119,1.03034803,17.50917807,51.74515101,0.3556096,0.52499112,22.86733258,0.46901859,1.15915747,0.61520843,1.0209175,0.61126531])
                mean_list = np.array([54.366337,0.683168,0.966997,131.623762,246.264026,0.148515,0.528053,149.646865,0.326733,1.039604,1.399340,0.729373,2.313531])
                
                params = (params - mean_list)/(std_list)
                
                #reshaping for need of predictors
                params = params.reshape(1,-1)

                #Predicting ANN result
                ann_res = 0
                with graph.as_default():
                    ann_res = int(ann['model'].predict(params)[0][0])

                knn_res = int(knn['model'].predict(params)[0])

                #Making predictions
                predictions = list([
                    {'ann':ann_res},
                    {'knn':knn_res},
                    {'dtree':int(dtree['model'].predict(params)[0])},
                    {'logisticRegression':int(lr['model'].predict(params)[0])},
                    {'naiveBayes':int(nb['model'].predict(params)[0])},
                    {'svm':int(svm['model'].predict(params)[0])},
                ])

                #Accuracy of pre-trained models
                accuracy = list([
                    {'ann':ann['accuracy']},
                    {'knn':knn['accuracy']},
                    {'decisionTree':dtree['accuracy']},
                    {'logisticRegression':lr['accuracy']},
                    {'naiveBayes':nb['accuracy']},
                    {'svm':svm['accuracy']}
                ])
                
                return jsonify(Result = knn_res,Predictions = predictions, Accuracy = accuracy)


        #If anything goes wrong.
        except Exception as e:
            return jsonify(error = 'Sorry, some exception has occurred.', for_dev = str(e))


class BreastCancerAPI(Resource):

    def get(self,params):

        global bc_knn,bc_lr,bc_nb,bc_svm

        try:
            #Taking params into a list
            params = params.split('-')
            params = [float(f) for f in params]
            
            if len(params)!=10:
                raise Exception('You need to enter 13 params')

            else:

                #numpy array
                params = np.array(params)

                #Feature scaling          
                mean_list = np.array([-3.162867e-15,-6.530609e-15,-7.078891e-16,-8.799835e-16,6.056471e-15,-1.146905e-15,-4.421380e-16,1.017347e-15,-1.965426e-15,5.289657e-16])
                std_list = np.array([1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0])

                params = (params - mean_list)/(std_list)

                params = params.reshape(1,-1)

                #Accuracy of pre-trained models
                accuracy = list([
                    {'knn':bc_knn['accuracy']},
                    {'lr':bc_lr['accuracy']},
                    {'nb':bc_nb['accuracy']},
                    {'svm':bc_svm['accuracy']},
                ])

                #Making predictions
                svm_res = bc_svm['model'].predict(params)[0]

                predictions = list([
                    {'knn':bc_knn['model'].predict(params)[0]},
                    {'lr':bc_lr['model'].predict(params)[0]},
                    {'nb':bc_nb['model'].predict(params)[0]},
                    {'svm':svm_res},
                ])

                return jsonify(Result = svm_res,Predictions = predictions, Accuracy = accuracy)

        #If anything goes wrong.
        except Exception as e:
            return jsonify(error = 'Sorry, some exception has occurred.', for_dev = str(e))

        return jsonify(msg = 'breast cancer API', params = params)


api.add_resource(HeartAPI,'/heart/predict/<string:params>')
api.add_resource(BreastCancerAPI,'/cancer/predict/<string:params>')

#Running of app
if __name__ == '__main__':
    app.run()


