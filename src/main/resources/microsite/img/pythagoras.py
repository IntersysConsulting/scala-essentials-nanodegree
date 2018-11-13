#!/usr/bin/env python

import numpy as np
import matplotlib.pyplot as plt

V  = np.array([[0,3], [4,0], [-4,3]])
XY = np.array([[0,0], [0,0], [ 4,0]])

plt.close('all')
plt.quiver(XY[:,0], XY[:,1], V[:,0], V[:,1],
           color=['r','b','g'],
           angles='xy',
           scale_units='xy',
           scale=1)
plt.axis('equal')
plt.xlim((-0.1, 4.1))
plt.ylim((0.0, 3.0))
plt.savefig("simple-distance.png")
