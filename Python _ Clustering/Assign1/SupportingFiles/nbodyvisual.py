#!/usr/bin/python
# -*- coding: utf-8 -*-

"""Interactive visual front end to NBody implementation"""

import time

# Tell pylint that Axes3D import is required although never explicitly used

from mpl_toolkits.mplot3d import Axes3D  # pylint: disable=W0611
import matplotlib.pyplot as plt

from nbodyphysics1 import move, random_galaxy


def gfx_init(xm, ym, zm):
    """Init plot"""

    plt.ion()
    fig = plt.figure()
    sub = fig.add_subplot(111, projection='3d')
    sub.xm = xm
    sub.ym = ym
    sub.zm = zm
    return sub


def show(sub, galaxy):
    """Show plot"""

    xpos = [galaxy[1]]
    ypos = [galaxy[2]]
    zpos = [galaxy[3]]
    bsiz = [galaxy[0]]

    sub.clear()
    sub.scatter(
        xpos,
        ypos,
        zpos,
        s=bsiz,
        marker='o',
        c='blue',
        )
    sub.set_xbound(-sub.xm, sub.xm)
    sub.set_ybound(-sub.ym, sub.ym)
    try:
        sub.set_zbound(-sub.zm, sub.zm)
    except AttributeError:
        print 'Warning: correct 3D plots may require matplotlib-1.1 or later'
    plt.draw()
    plt.pause(0.000000001)


def nbody_debug(bodies, time_step):
    """Run simulation with visualization"""

    x_max = 500
    y_max = 500
    z_max = 500
    galaxy = random_galaxy(x_max, y_max, z_max, bodies)
    gfx = gfx_init(x_max, y_max, z_max)
    dt = 1000.0  # One hundred year timesteps ensures that we see movement

    start = time.time()
    for _ in range(time_step):
        move(galaxy, dt)
        show(gfx, galaxy)
    stop = time.time()
    print 'Simulated ' + str(bodies) + ' bodies for ' + str(time_step) \
        + ' timesteps in ' + str(stop - start) + ' seconds'


if __name__ == '__main__':
    nbody_debug(300, 1000)

