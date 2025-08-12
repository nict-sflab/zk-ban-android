package zkbancrypto

import (
	_ "embed"
	"fmt"

	client "github.com/akakou/zk-ban-system/client/signer"
	"github.com/akakou/zk-ban-system/utils"
	_ "golang.org/x/mobile/bind"
)

func RequestJoin(idToken, url string) ([]byte, error) {
	utils.PeriodUnit = utils.HalfMinutes
	fmt.Printf("Period: %v\n", utils.Period())
	return client.RequestJoin(idToken, url)
}

func Sign(message []byte, count int64, signer, gpk []byte, url string) ([]byte, error) {
	return client.Sign(message, count, signer, gpk, url)
}

func RequestUpdate(signer, rl, gpk []byte, url string) ([]byte, error) {
	utils.PeriodUnit = utils.HalfMinutes
	return client.RequestUpdate(signer, rl, gpk, url)
}

func FetchGroupPublicKey(url string) ([]byte, error) {
	return client.FetchGroupPublicKey(url)
}

func FetchRevocationList(signer []byte, url string) ([]byte, error) {
	return client.FetchRevocationList(signer, url)
}
