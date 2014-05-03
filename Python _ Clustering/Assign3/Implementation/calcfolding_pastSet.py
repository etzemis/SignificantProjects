import time

def folding(pset,protein, savebest,workers):
    """Folding simulation"""
    plen = len(protein)
    mapp = []

    '''initialize pastset parameters'''
    #pset = pastset.PastSet()
    ProtBest = pset.enter(('Result', int,list))
    pset.delelement(ProtBest)
    ProtBest = pset.enter(('Result', int,list))
    ProtJobs = pset.enter(('Protein-jobs',list,list)) # map, place
    pset.delelement(ProtJobs)
    ProtJobs = pset.enter(('Protein-jobs',list,list)) # map, place
    ProtSeries = pset.enter(('Protein',list))   # protein

    '''insert the protein inside the tuple'''
    ProtSeries.move((protein,))

    '''create all the workers'''
    for i in xrange(workers):
        pset.spawn('a3/calcWorker.py', '')
    for i in range(2 * plen + 1):
        mapp.append((2 * plen + 1) * [' '])

    '''initialize map and place'''
    r = mapp[plen]
    r[plen] = protein[0]
    place = [(plen, plen)]

    '''calculate folding and put semi-finshed mpas in tuple space'''
    counter = fold(protein, mapp, place, ProtJobs,0)
    print 'counter is', counter
    '''take best result from each worker and calculate best'''
    best = -9999999
    #print ' first is ', ProtBest.first(), 'last is ', ProtBest.last()
    for i in xrange(counter):
        (bestscore, winner) = ProtBest.observe()
        #print bestscore
        if bestscore > best:
            best = bestscore
    #print ' first is ', ProtBest.first(), 'last is ', ProtBest.last()
    return (best,[])
    #pset.halt()







def fold(
    protein,
    mapp,
    place,
    ProtJobs,
    counter,
    ):
    """Fold prototein"""
    cnt = len(place)
    #print 'count is', cnt
    if cnt == 1:
        xspace = [1]
        yspace = []
    elif cnt == 2:
        xspace = [1]
        yspace = [1]
    elif cnt == 3:
        xspace = [-1, 1]
        yspace = [1]
#    elif cnt == 4:
#        xspace = [-1,1]
#        yspace = [-1,1]
    elif cnt >=8:
        #put it in the tuple space
        #print 'putting to the tuple space'
        ProtJobs.move((mapp,place))
        #print 'jobs last counter is', ProtJobs.last()
        #print 'place is ', place
        counter = counter+1
        return counter
    else:
        xspace = [-1,1]
        yspace = [-1,1]
    for dx in xspace:
        #print 'dx = ', dx
        (newx, newy) = place[-1]
        newx += dx
        if mapp[newx][newy] == ' ':
            mapp[newx][newy] = protein[cnt]
            place.append((newx, newy))
            counter = fold(protein, mapp, place, ProtJobs, counter)
            mapp[newx][newy] = ' '
            place.pop()
        #else:
            #print 'ommited porition', mapp[newx][newy]

    for dy in yspace:
        (newx, newy) = place[-1]
        newy += dy
        if mapp[newx][newy] == ' ':
            mapp[newx][newy] = protein[cnt]
            place.append((newx, newy))
            counter = fold(protein, mapp, place, ProtJobs, counter)
            mapp[newx][newy] = ' '
            place.pop()
        #else:
        #    print 'ommited porition', mapp[newx][newy]
    return counter