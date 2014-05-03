#!/usr/bin/python
# -*- coding: utf-8 -*-

"""NBody in N^2 complexity
Note that we are using only Newtonian forces and do not consider relativity
Neither do we consider collisions between stars
Thus some of our stars will accelerate to speeds beyond c
This is done to keep the simulation simple enough for teaching purposes

All the work is done in the calc_force, move and random_galaxy functions.
To vectorize the code these are the functions to transform.
"""
import numpy

# By using the solar-mass as the mass unit and years as the standard time-unit
# the gravitational constant becomes 1

G = 1.0


def calc_force(a, b, dt):
    """Calculate forces between bodies
    F = ((G m_a m_b)/r^2)/((x_b-x_a)/r)
    """

    #r = ((b[:,'x'] - a['x']) ** 2 + (b[:,'y'] - a['y']) ** 2 + (b[:,'z'] - a['z']) ** 2) ** 0.5
    
    r = ((b[1] - a[1]) ** 2 + (b[2] - a[2]) ** 2 + (b[3] - a[3]) ** 2) ** 0.5
    a[4] += numpy.sum(numpy.nan_to_num(G * a[0] * b[0] / r ** 2 * ((b[1] - a[1]) / r) / a[0] * dt ))
    a[5] += numpy.sum(numpy.nan_to_num(G * a[0] * b[0] / r ** 2 * ((b[2] - a[2]) / r) / a[0] * dt ))
    a[6] += numpy.sum(numpy.nan_to_num(G * a[0] * b[0] / r ** 2 * ((b[3] - a[3]) / r) / a[0] * dt ))

def move(a,galaxy, dt):
    """Move the bodies
    first find forces and change velocity and then move positions
    """

  #  for i in galaxy:
  #      for j in galaxy:
  #          if i != j:
  #             calc_force(i, j, dt)
    
    for i in a:
        calc_force(i[0], galaxy, dt)
                
  #  for i in galaxy:
  #      i['x'] += i['vx']
  #      i['y'] += i['vy']
  #      i['z'] += i['vz']
    galaxy[1] += galaxy[4]
    galaxy[2] += galaxy[5]
    galaxy[3] += galaxy[6]

def random_galaxy(
    x_max,
    y_max,
    z_max,
    n,
    ):
    """Generate a galaxy of random bodies"""

    max_mass = 40.0  # Best guess of maximum known star

    # We let all bodies stand still initially

    # mydescriptor = {'names': ('m','x','y','z','vx','vy','vz'), 'formats': ('float64', 'float64', 'float64','float64', 'float64', 'float64', 'float64')}
    m = numpy.random.random(n) * numpy.random.randint(1, max_mass,n)/ (4 * numpy.pi ** 2)
    x = numpy.random.randint(-x_max, x_max,n)
    y = numpy.random.randint(-y_max, y_max,n)
    z = numpy.random.randint(-z_max, z_max,n)
    vx= numpy.ones(n)
    return numpy.array([[m],[x],[y],[z],[vx],[vx],[vx]])
# , dtype=mydescriptor)

#for _ in xrange(n)
if __name__ == '__main__':
    g = random_galaxy(10, 10,10,5)
    move(g,2)