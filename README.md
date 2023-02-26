# Model of Data input 
## Input file example:

| [NumberOfOrigins] | [NumberOfDestinies] | [...] | [N] |
| :-----------: | :-----------: | :-----------: | :-----------: |
| [Offer1] | [Offer2] | ... | [OfferN] |
| [Demand1] | [Demand2] | ... | [DemandN] |
| [Cost_1_1] | [Cost_1_2] | ... | [Cost_1_j] |
| [Cost_2_1] | ... | ... | ... |
| [Cost_3_1] | ... | ... | ... |
| ... | ... | ... | ... |
| [Cost_i] | [Cost_i_2] | ... | [Cost_i_j] |

## Actual input file example following the model:
```
4 2        <- Number of offers of N length.
9 12 7 15  <- Number of demands of N length.
7 6        <- Number of costs of j horizontal length and i vertical length.
8 2
7 3
2 10
9 8
```