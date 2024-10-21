Here's a cleaned-up version of your notes, formatted in a `README.md` style, with additional clarity and structure:

---

## LiveScoreBoardService - Design Decisions

### Data Storage Approach
I used a `HashMap<Match, Score>` to store the data in the scoreboard. The **`Match`** object acts as the key, and the **`Score`** object serves as the value.

- **Why `HashMap`?**
    - I needed an efficient way to store and access match data by using the match itself as the identifier. The constant-time complexity of `HashMap` lookups (`O(1)`) made it an ideal choice over alternatives like a `Set`, where I'd have to iterate over the collection to find specific elements.

- **Why Not a `Set`?**
    - A `Set` could have been used for storing matches directly, but I would have needed to iterate through it every time I wanted to find a specific match. The `HashMap` allows for more efficient lookups, especially when dealing with a larger number of matches.

### Logging
I did not write explicit logs for operations related to the datastore (i.e., the `HashMap`) because I assumed the project would eventually be backed by a database. In real-world systems, logging at the datastore level can become redundant when such operations are handled by the underlying database engine.

