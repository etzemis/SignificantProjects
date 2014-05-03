#!/usr/bin/python
# -*- coding: utf-8 -*-

"""Prototein - simplified protein folding"""

from calcfolding import folding
"""Visualizer for Prototein simulation"""

import matplotlib.pyplot as plt

ax = None


def gfx_init():
    """Init plot"""

    global ax
    plt.ion()
    fig = plt.figure()
    ax = fig.add_subplot(111)

    return ax


def show_protein(place, protein):
    """Show plot"""

    global ax

    ax.clear()
    prev = None
    cnt = 0
    x = [i[0] for i in place]
    y = [i[1] for i in place]

    xmin = min(x)
    xmax = max(x)
    ymin = min(y)
    ymax = max(y)

    Hi = [i for i in xrange(len(protein)) if protein[i] == 'H']
    Hx = [place[i][0] for i in Hi]
    Hy = [place[i][1] for i in Hi]
    Pi = [i for i in xrange(len(protein)) if protein[i] == 'P']
    Px = [place[i][0] for i in Pi]
    Py = [place[i][1] for i in Pi]
    ax.plot(x, y, 'y-', linewidth=2)
    ax.plot(Hx, Hy, 'ro', ms=15.0)
    ax.plot(Px, Py, 'bo', ms=15.0)
    ax.set_xbound(xmin - 1, xmax + 1)
    ax.set_ybound(ymin - 1, ymax + 1)
    plt.draw()


def gfx_end():
    """End plot"""

    plt.ioff()
    plt.show()

class BestKnown:
    def __init__(self):
        gfx_init()

    def save(self, winner, protein):
        show_protein(winner, protein)

    def finish(self, winner, protein):
        print 'Close graphics window to terminate...'
        show_protein(winner, protein)
        gfx_end()


# The below line does a single folding with graphics

best = BestKnown()

folding('HHHHPPHPPHHHH', best)


# Just an example of how high your compute-time gets with a few more aminoacids
# Don't use this for anything other than personal fun
# folding('HPPHPPPPPHHHHPPPPPHPPH', best)

