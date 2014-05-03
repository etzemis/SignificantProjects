#Throughout the file teh savebest parameter is only used for debugging
#so you need not implement this parameter if you do not want it!

""" We eliminste the simplest symmetries:

......
..01..  No need to try up,down or left since they
......  would be rotations of the same
......

...2..
..012.  No need to try the down since it would be a mirror
......
......

...3..
..323. No need to try the down since it would be a mirror
..0123
......

No more symetries are eliminated.
"""

import copy

def calc_score(
    place,
    map,
    protein,
    best,
    savebest,
    ):
    """Evaluate score"""

    score = 0
    (bestscore, winner) = best

    for i in place:
        (x, y) = i
        if map[x][y] == 'H':
            if map[x - 1][y] == ' ':
                score -= 1
            if map[x][y - 1] == ' ':
                score -= 1
            if map[x + 1][y] == ' ':
                score -= 1
            if map[x][y + 1] == ' ':
                score -= 1

    if score > bestscore:
        bestscore = score
        winner = copy.copy(place)
        savebest.save(winner, protein)
        return (bestscore, winner)

    return None


def fold(
    protein,
    map,
    place,
    best,
    savebest,
    ):
    """Fold prototein"""
    cnt = len(place)
    if cnt == len(protein):
        return calc_score(place, map, protein, best, savebest)

    elif cnt == 1:
        xspace = [1]
        yspace = []
    elif cnt == 2:
        xspace = [1]
        yspace = [1]
    elif cnt == 3:
        xspace = [-1, 1]
        yspace = [1]
    else:
        xspace = [-1,1]
        yspace = [-1,1]

    for dx in xspace:
        (newx, newy) = place[-1]
        newx += dx
        if map[newx][newy] == ' ':
            map[newx][newy] = protein[cnt]
            place.append((newx, newy))
            res = fold(protein, map, place, best, savebest)
            map[newx][newy] = ' '
            place.pop()
            if res:
                best = res

    for dy in yspace:
        (newx, newy) = place[-1]
        newy += dy
        if map[newx][newy] == ' ':
            map[newx][newy] = protein[cnt]
            place.append((newx, newy))
            res = fold(protein, map, place, best, savebest)
            map[newx][newy] = ' '
            place.pop()
            if res:
                best = res

    return best


def folding(protein, savebest):
    """Folding simulation"""
    plen = len(protein)
    map = []

    for i in range(2 * plen + 1):
        map.append((2 * plen + 1) * [' '])

    r = map[plen]
    r[plen] = protein[0]
    place = [(plen, plen)]

    (bestscore, winner) = fold(protein, map, place, (-999999, []), savebest)
    print ' best score is', bestscore
    savebest.finish(winner, protein)

