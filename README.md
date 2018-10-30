# filmes-famosos
This project works by retrieving data from an external API (themoviedb.org). Considering this, it is necessary a valid API key to perform data consuming on RESTful services.

So, the two steps above are necessary to get things done:

1 - Get a valid key
For more information about how to get a key, check out: developers.themoviedb.org.

2 - Put the key in your respective "gradle.properties" file
Find the .gradle folder in your home directory. Usually it can be found at:

Windows: C:\Users\<Your Username>\.gradle
Mac: /Users/<Your Username>/.gradle
Linux: /home/<Your Username>/.gradle

Inside it there would be a file named gradle.properties (just create it if there isn’t any).

After that, add your key to the file as a property. If your key was "my-tmdb-api-key", the file after adding the key might look something like:

TMDB_APIKey="my-tmdb-api-key"

Enjoy!