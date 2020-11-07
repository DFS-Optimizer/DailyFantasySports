from sklearn.linear_model import LinearRegression
from sklearn.cluster import KMeans
from sklearn import metrics
from sklearn.model_selection import train_test_split
import pandas as pd
from Database.query import SqlQuery
import matplotlib.pyplot as plt
import seaborn as sns
import pickle


class Model:
    model = None

    def pickle(self, fname):
        pickle.dump(self.model, open(fname, 'wb'))


class LinearModel(Model):
    y_prediction = None

    def __init__(self, dataframe, y_loc):
        y = dataframe.iloc[:, y_loc]
        x = dataframe.drop(dataframe.columns[[y_loc]], axis=1)
        self.x_train, self.x_test, self.y_train, self.y_test = train_test_split(x, y, random_state=1)
        self.model = self.train()

    def train(self):
        return LinearRegression(normalize=True).fit(self.x_train, self.y_train)

    def get_prediction(self):
        self.y_prediction = self.model.predict(self.x_test)

    def get_results(self):
        print(self.model.intercept_)
        print(self.model.coef_)
        print(pd.DataFrame({"prediction":self.y_prediction, "real": self.y_test}, columns=["prediction", "real"]).head(50))
        print('R squared: %.2f' % metrics.r2_score(self.y_test, self.y_prediction))

    def visualize(self):
        data_set = pd.concat([self.y_train, self.x_train], axis=1, sort=False)
        g = sns.pairplot(data_set, x_vars=self.x_train.keys(),
                         y_vars=["fantasy_points_scored"], kind="reg", plot_kws={'line_kws':{'color':'red'}})
        g.map(plt.scatter)
        plt.show()


class KNearest(Model):

    def __init__(self, dataframe, clusters):
        self.clusters = clusters
        self.dataframe = dataframe
        self.model = self.train()

    # returns data frame of stats and assigned cluster/role
    def train(self):
        kmeans = KMeans(n_clusters=self.clusters).fit(self.dataframe) # model
        self.dataframe['role'] = kmeans.predict(self.dataframe)   # set dataframe role column to kmeans results
        return kmeans

    def get_results(self):
        print(self.dataframe.head(25))

    def get_score(self):
        return self.model.score(self.dataframe)


def elbow_curve(dataframe):
    # elbow method for k means to decide on optimal number of clusters
    Nc = range(1, 20)
    kmeans = [KNearest(dataframe, clusters=i) for i in Nc]
    score = [kmeans[i].model.score(dataframe) for i in range(len(kmeans))]
    plt.plot(Nc, score)
    plt.xlabel('Number of Clusters')
    plt.ylabel('Score')
    plt.title('Elbow Curve')
    plt.show()


# #put training data in roles
# with SqlQuery() as query:
#     df = query.get_table("model_past6",  dataframe=True)
#
# player = df.iloc[:, 0]
# season = df.iloc[:, 1]
# others = df.iloc[:, 3:6]
#
# training = df.iloc[:, 6:21]
# kmeans = KNearest(training, clusters=5)
# #r =kmeans.dataframe.iloc[:,15]
# print(r)
# setup = pd.concat([player, season, r], axis = 1)
# setup = setup.iloc[:, 0:3]
#
# trained = pd.concat([setup, others, training], axis = 1)
#
# # with SqlQuery() as query:
# #      query.insert_many(trained, 'model_trained6')
# #
# setup.drop_duplicates(subset=['player_id', 'season_id'], keep='first', inplace=True)
# query.insert_many(setup, "roles")
#


with SqlQuery() as query:
    df = query.get_table("model_past6",  dataframe=True)
one = df.iloc[:, 2:4]
two = df.iloc[:, 6:21]
training = pd.concat([one,two], axis = 1)
kmeans = KNearest()
pickle.dump(kmeans, open("save.pkl", "wb"))
model = LinearModel(training, 1)
model.get_prediction()
model.get_results()
