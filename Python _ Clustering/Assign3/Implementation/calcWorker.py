import pastset
import copy



def calc_score(
    place,
    mapp,
    protein,
    best,
    ):
    """Evaluate score"""

    score = 0
    (bestscore, winner) = best

    for i in place:
        (x, y) = i
        if mapp[x][y] == 'H':
            if x != 0:
                if mapp[x - 1][y] == ' ':
                    score -= 1
            if y!=0:
                if mapp[x][y - 1] == ' ':
                    score -= 1
            if x != 2*len(protein):
                if mapp[x + 1][y] == ' ':
                    score -= 1
            if y != 2*len(protein):
                if mapp[x][y + 1] == ' ':
                    score -= 1

    if score > bestscore:
        bestscore = score
        winner = copy.copy(place)
        return (bestscore, winner)
    else:
        return best



def foldWorker(
    protein,
    mapp,
    place,
    best,
    ):
    """Fold prototein"""
    #print ' ******WORKER******* place = ', place
    cnt = len(place)
    if cnt == len(protein):
        return calc_score(place, map, protein, best)
    else:
        xspace = [-1,1]
        yspace = [-1,1]

    for dx in xspace:
        (newx, newy) = place[-1]
        newx += dx
        if mapp[newx][newy] == ' ':
            mapp[newx][newy] = protein[cnt]
            place.append((newx, newy))
            best = foldWorker(protein, mapp, place, best)
            mapp[newx][newy] = ' '
            place.pop()

    for dy in yspace:
        (newx, newy) = place[-1]
        newy += dy
        if mapp[newx][newy] == ' ':
            mapp[newx][newy] = protein[cnt]
            place.append((newx, newy))
            best = foldWorker(protein, mapp, place, best)
            mapp[newx][newy] = ' '
            place.pop()
    return best



pset = pastset.PastSet()
#print 'initialized pastset'
ProtBest = pset.enter(('Result', int,list))
ProtJobs = pset.enter(('Protein-jobs',list,list)) # map, place
ProtSeries = pset.enter(('Protein',list))   # protein

while True:
    protein = ProtSeries.observe(ProtSeries.last()-1)[0] #does not increase the counter
    (map,place) = ProtJobs.observe()
    (best, winner) = foldWorker(protein, map, place,(-9999999,[]))
    ProtBest.move((best,winner))
#pset.halt()
