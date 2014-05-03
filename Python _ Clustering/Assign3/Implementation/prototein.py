#!/usr/bin/python
# -*- coding: utf-8 -*-

"""Prototein - simplified protein folding"""


import time
import pastset
import sys

from calcfolding_pastSet import folding
#from calcfolding import folding

class BestKnown:
    def __init__(self):
        pass

    def save(self, winner, protein):
        pass

    def finish(self, winner, protein):
        pass



workers = 32
if sys.argv[1:] and sys.argv[1].isdigit():
    workers = int(sys.argv[1])

p = 'PHHPHHPHHPHHPHHHP'


print 'Number of Cpus used = ' , workers
best = BestKnown()
pset = pastset.PastSet()
start=time.time()
(bestscore, winner) = folding(pset,p, best, workers)
stop=time.time()
print 'Length ',len(p),'took',stop-start,'sec'
print 'BestScore was ', bestscore
pset.halt()


