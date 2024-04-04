# Mars Explorer

University project for agent based architecture, using [JADE](https://jade.tilab.com) library.

The goal was to have few martian explorators collecting minerals, working independently, and communicating via events with the lander module. The UI is a simple HTML table with different colors for cells :)
The agents are unaware of their surroundings, they work in random directions. They don't know where they are, there is no GPS on Mars. Yet. So only way they can come back to lander module is by signal it emits, like a radio beacon, that gets stronger the closer the explorer is to lander.
