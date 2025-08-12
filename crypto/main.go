package zkbancrypto

import (
	_ "embed"
	"encoding/json"
	"flag"
	"fmt"
	"testing"

	client "github.com/akakou/zk-ban-system/client/signer"
	"github.com/akakou/zk-ban-system/utils"
	t "github.com/akakou/zk-ban/test"
	_ "golang.org/x/mobile/bind"
)

func RequestJoin(idToken, url string) ([]byte, error) {
	newSigner, err := client.RequestJoin(idToken, url)

	return newSigner, err
}

func Sign(message []byte, count int64, signer, gpk []byte, url string) ([]byte, error) {
	signature, err := client.Sign(message, count, signer, gpk, url)
	if err != nil {
		return nil, err
	}

	res, err := json.Marshal(signature)
	if err != nil {
		return nil, err
	}

	return res, nil
}

func RequestUpdate(signer, rl, gpk []byte, url string) ([]byte, error) {
	newSigner, err := client.RequestUpdate(signer, rl, gpk, url)
	return newSigner, err
}

func FetchGroupPublicKey(url string) ([]byte, error) {
	return client.FetchGroupPublicKey(url)
}

func FetchRevocationList(signer []byte, url string) ([]byte, error) {
	return client.FetchRevocationList(signer, url)
}

func SetConstantPeriodForDebug(i int) {
	today := func() int64 {
		return int64(i)
	}

	utils.Today = today
}

func Benchmark() {
	loops := 10

	testing.Init()

	flag.Set("test.benchtime", fmt.Sprintf("%dx", loops))
	flag.Set("test.bench", "github.com/akakou/zk-ban/test")
	flag.Parse()

	testing.Benchmark(t.BenchmarkAll)
	testing.Benchmark(t.BenchmarkUpdate)
}

// func main() {
// 	Benchmark()
// }
