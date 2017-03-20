library(rpart)
tennis <- read.table("tennis_data.txt")
#~ tennis
ad.tennis.cnt <- rpart.control (minsplit = 1)
ad.tennis <- rpart(Jouer~Vent+Temperature+Ciel+Humidite, tennis, control = ad.tennis.cnt)
#~ ad.tennis
#~ plot (ad.tennis)
#~ text (ad.tennis)
#~ plot (ad.tennis, uniform=T)
#~ text (ad.tennis, use.n=T, all=T)
#~ plot (ad.tennis, branch=.7)
#~ text (ad.tennis, use.n=T)
#~ plot (ad.tennis, branch=.4, uniform=T, compress=T)
#~ text (ad.tennis, all=T,use.n=T)
#~ plot (ad.tennis, branch=.2, uniform=T, compress=T, margin=.1)
#~ text (ad.tennis, all=T, use.n=T, fancy=T)
#~ predict(ad.tennis, tennis)
tennis2 <- read.table("tennis_data_2.txt")
#~ tennis2
predict(ad.tennis, tennis2)
#~ car.test.frame
ad.car.cnt <- rpart.control (minsplit = 1)
ad.car <- rpart(Type~Price+Country+Reliability+Mileage+Weight+Disp.+HP, car.test.frame, control = ad.car.cnt)
ad.car.prune <- prune(ad.car, 0.03)
#~ plot (ad.car.prune)
#~ text (ad.car.prune)
ad.car$cptable

