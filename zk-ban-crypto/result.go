package zkbancrypto

import "encoding/json"

type Result struct {
	Result any   `json:"result"`
	Error  error `json:"error"`
}

func (result *Result) Output() []byte {
	buf, _ := json.Marshal(result)
	return buf
}
