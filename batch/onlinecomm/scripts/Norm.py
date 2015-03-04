__author__ = 'Iosu'



class Norm(object):
    id = 0
    name = ""
    edges = []
    weight = 0

    # The class "constructor" - It's actually an initializer
    def __init__(self, id, name, edges, weight):
        self.id = id
        self.name = name
        self.edges = edges
        self.weight = weight

def make_norm(id, name, edges, weight):
    norm = Norm(id, name, edges, weight)
    return norm

