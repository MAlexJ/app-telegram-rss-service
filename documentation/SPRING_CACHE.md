#### @Cachable on methods without input parameters?

Link: https://stackoverflow.com/questions/48888760/cachable-on-methods-without-input-parameters

```
@Cacheable(value = CACHE, key = "#root.method.name")
```