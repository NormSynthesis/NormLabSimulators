#!/usr/bin/env python
"""

"""
__author__ = """Iosu Mendizabal (iosu@iiia.csic.es)"""

import Norm as nor
import sys
import networkx as nx
import matplotlib.pyplot as plt
import os
try:
    from networkx import graphviz_layout
except ImportError:
    raise ImportError("This example needs Graphviz and either PyGraphviz or Pydot")


def load_graph(dataFile):
    """ Return the norms object from de norms.txt database.
    """
    # fh = open('norms.txt', 'r')
    norms = []
    i = 0
    with open(dataFile, "r") as f:
        for line in f:
            line = line.decode()
            if line.startswith("*"):  # skip comments
                continue

            if line.startswith("n:"): # this line creates a norm
                i += 1
                norm_id = i
                norm_name = ""
                norm_edges = []
                norm_weight = 0
                (code, node) = line.split(":")
                (norm, nec) = node.split(".")
                norm_weight = float(nec) * 10.0
                norm_name = norm
                norm = nor.make_norm(norm_id, norm_name, norm_edges, norm_weight)
                norms.append(norm)

            if line.startswith("e:"): # this line are the edges
                (code, connections) = line.split(":")
                edges = connections.split()
                for e in edges:
                    norm_edges.append(e)
                norms[norms.__len__()-1].edges = norm_edges
    return norms

def create_normGraph(norms):

    G = nx.DiGraph()
    weight = {}

    for norm in norms:
        G.add_node(norm.name)
        weight[norm] = norm.weight

    for norm in norms:
        for edge in norm.edges:
            G.add_edge(norm.name, edge)
    return G, weight

def plot_graph(G, weight):
    # # draw with matplotlib/pylab

    try:
        plt.figure(figsize=(8, 8))

        # with nodes colored by degree sized by necessity
        node_color = [float(G.degree(v)) for v in G]
        pos = nx.graphviz_layout(G)

        nx.draw(G,
                pos,
                node_size=[weight[v] for v in weight],
                alpha=0.5,
                node_color=node_color,
                with_labels=True)


        # plt.show()
        base = os.path.splitext(dataFile)[0]

        plt.savefig(base+".png")
    except:
        print "Error: ", sys.exc_info()
        pass

if __name__ == '__main__':
    dataFile = sys.argv[1]
    norms = load_graph(dataFile)
    G, weight = create_normGraph(norms)
    plot_graph(G, weight)

    # nx.draw(G)
    # plt.show()